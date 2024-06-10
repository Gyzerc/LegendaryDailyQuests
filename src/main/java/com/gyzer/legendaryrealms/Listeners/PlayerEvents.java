package com.gyzer.legendaryrealms.Listeners;

import com.gyzer.legendaryrealms.Data.Quest.Checker.EntityGoalChecker;
import com.gyzer.legendaryrealms.Data.Quest.Checker.GoalNoCheck;
import com.gyzer.legendaryrealms.Data.Quest.Checker.ItemGoalChecker;
import com.gyzer.legendaryrealms.Data.Quest.Checker.StringGoalChecker;
import com.gyzer.legendaryrealms.Data.Quest.Objective.ObjectiveType;
import com.gyzer.legendaryrealms.Data.User.PlayerData;
import com.gyzer.legendaryrealms.LegendaryDailyQuests;
import com.gyzer.legendaryrealms.Menu.MenuPanel;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;
import java.util.logging.Level;

public class PlayerEvents implements Listener {
    final LegendaryDailyQuests legendaryDailyQuests = LegendaryDailyQuests.getLegendaryDailyQuests();

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        PlayerData data = legendaryDailyQuests.getPlayerDataManager().getPlayerData(p);
        data.checkQuests();
    }

    @EventHandler
    public void onInvclick(InventoryClickEvent e){
        if (e.getInventory().getHolder() instanceof MenuPanel){
            e.setCancelled(true);
            legendaryDailyQuests.sync(()->((MenuPanel) e.getInventory().getHolder()).onClick(e));
            return;
        }
        if (!e.isCancelled()) {
            ItemStack i = e.getCurrentItem();
            if (i != null && !i.getType().equals(Material.AIR)) {
                Player p = (Player) e.getWhoClicked();
                if (e.getInventory().getType() == InventoryType.MERCHANT && e.getSlotType() == InventoryType.SlotType.RESULT) {
                    MerchantInventory inventory = (MerchantInventory) e.getClickedInventory();
                    InventoryHolder holder = e.getClickedInventory().getHolder();
                    if (holder instanceof Villager) {
                        if (inventory.getSelectedRecipe() != null) {
                            new GoalNoCheck().addProgress(p,ObjectiveType.TRADE,1);
                        }
                    }
                }
            }
        }
    }




    @EventHandler
    public void onKillEntity(EntityDeathEvent e){
        if (e.getEntity() instanceof Player) {
            new GoalNoCheck().check((Player) e.getEntity(),ObjectiveType.DEATH,1);
        }
        if (e.getEntity().getKiller() != null){
            Player p = e.getEntity().getKiller();
            if (e.getEntity().getCustomName() != null) {
                new StringGoalChecker(e.getEntity().getCustomName()).check(p,ObjectiveType.KILL_CUSTOM,1);
            }
            else {
                new EntityGoalChecker(e.getEntity()).check(p, ObjectiveType.KILL_ENTITY,1);
            }
        }
    }


    @EventHandler
    public void onMove(PlayerMoveEvent e)
    {
        if (!e.isCancelled()) {
            Player p = e.getPlayer();
            if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) {
                return;
            }
            if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) {
                return;
            }
            double juli = e.getFrom().distance(e.getTo());
            new GoalNoCheck().check(p,ObjectiveType.MOVE,juli);
        }
    }

    @EventHandler
    public void onGainExp(PlayerExpChangeEvent e) {
        new GoalNoCheck().check(e.getPlayer(),ObjectiveType.GAIN_EXP,e.getAmount());
    }
    @EventHandler
    public void onGainLevel(PlayerLevelChangeEvent e){
        if (e.getNewLevel() > e.getOldLevel()) {
            new GoalNoCheck().check(e.getPlayer(),ObjectiveType.GAIN_LEVEL,(e.getNewLevel()-e.getOldLevel()));
        }
    }
    @EventHandler
    public void onFood(FoodLevelChangeEvent e) {
        if (!e.isCancelled()) {
            if (e.getEntity() instanceof Player) {
                Player p = (Player) e.getEntity();
                if (e.getFoodLevel() - p.getFoodLevel() > 0) {
                    double add = (e.getFoodLevel() - p.getFoodLevel());
                    new GoalNoCheck().check(p,ObjectiveType.GAIN_FOOD,add);
                } else {
                    double take = (p.getFoodLevel() - e.getFoodLevel());
                    new GoalNoCheck().check(p,ObjectiveType.TAKE_FOOD,take);
                }
            }
        }
    }

    @EventHandler
    public void onFish(PlayerFishEvent e) {
        if (!e.isCancelled()) {
            if (e.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
                Player p = e.getPlayer();
                if (e.getCaught() instanceof Item) {
                    Item i = (Item) e.getCaught();
                    ItemStack itemStack = i.getItemStack();
                    new ItemGoalChecker(itemStack).check(p,ObjectiveType.FISH,itemStack.getAmount());
                }
            }
        }
    }


    @EventHandler
    public void onBread(EntityBreedEvent e)
    {
        if (!e.isCancelled()) {
            if (e.getBreeder() instanceof Player) {
                Player p = (Player) e.getBreeder();
                new EntityGoalChecker(e.getEntity()).check(p,ObjectiveType.BREED,1);
            }
        }
    }

    @EventHandler
    public void onCook(FurnaceExtractEvent e){
        Player p = e.getPlayer();
        new ItemGoalChecker(new ItemStack(e.getItemType())).check(p,ObjectiveType.COOK,e.getItemAmount());
    }

    @EventHandler
    public void onShear(PlayerShearEntityEvent e)
    {
        if (!e.isCancelled()) {
            Player p = e.getPlayer();
            new EntityGoalChecker(e.getEntity()).check(p,ObjectiveType.SHEAR,1);
        }
    }
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!e.isCancelled()) {
            Player damagerIsPlayer = null;
            if (e.getEntity() instanceof Player) {
                Player p = (Player) e.getEntity();
                //受伤任务
                new GoalNoCheck().check(p,ObjectiveType.DAMAGED,e.getDamage());
                double blocking = e.getDamage(EntityDamageEvent.DamageModifier.BLOCKING);
                if (blocking != 0) {
                    //格挡伤害任务
                    new GoalNoCheck().check(p,ObjectiveType.BLOCKING,blocking);
                    //格挡远程伤害任务
                    if (e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
                        new GoalNoCheck().check(p,ObjectiveType.BLOCKING_PROJECTILE,blocking);
                    }
                }
            }
            if (e.getDamager() instanceof Player) {
                damagerIsPlayer = (Player) e.getDamager();
            }
            if (e.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow) e.getDamager();
                if (arrow.getShooter() instanceof Player) {
                    damagerIsPlayer = (Player) arrow.getShooter();
                    //造成远程伤害任务
                    new GoalNoCheck().check(damagerIsPlayer,ObjectiveType.PROJECTILE_DAMAGE,e.getDamage());
                }
            }
            if (damagerIsPlayer != null) {
                //造成伤害任务
                new GoalNoCheck().check(damagerIsPlayer,ObjectiveType.ATTACK_DAMAGE,e.getDamage());
            }
        }

    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e)
    {
        if (!e.isCancelled()) {
            Player p = e.getPlayer();
            new ItemGoalChecker(e.getItem()).check(p,ObjectiveType.CONSUME,1);
        }
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent e)
    {
        if (!e.isCancelled()) {
            Player p = e.getEnchanter();
            for (Map.Entry<Enchantment, Integer> enchantment : e.getEnchantsToAdd().entrySet()) {
                String id = enchantment.getKey().getKey().getKey().toUpperCase();
                new StringGoalChecker(id).check(p,ObjectiveType.ENCHANT,1);
            }
        }
    }

    @EventHandler
    public void onDestory(PlayerItemBreakEvent e)
    {
        Player p=e.getPlayer();
        new ItemGoalChecker(e.getBrokenItem()).check(p,ObjectiveType.ITEM_BROKEN,1);

    }

    @EventHandler
    public void onChat(PlayerChatEvent e)
    {
        if (!e.isCancelled()) {
            Player p = e.getPlayer();
            new GoalNoCheck().check(p,ObjectiveType.CHAT,1);
        }
    }

    @EventHandler
    public void onTame(EntityTameEvent e)
    {
        if (!e.isCancelled()) {
            if (e.getOwner() instanceof Player) {
                Player p = (Player) e.getOwner();
                new EntityGoalChecker(e.getEntity()).check(p,ObjectiveType.TAME,1);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
        if (!e.isCancelled()) {
            Player p = e.getPlayer();
            ItemStack i = p.getInventory().getItemInMainHand();
            if (i != null && i.getType().name().equals("BUCKET") && e.getRightClicked() instanceof Cow) {
                new GoalNoCheck().check(p,ObjectiveType.MILKING,1);
            }
        }
    }
//



}
