package com.beyazpoliss.bukkit;

import com.beyazpoliss.api.Provider;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.AllArgsConstructor;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
public class PlayerCacheProcessor implements Listener {

  private final Plugin instance;

  @EventHandler
  public void onJoin(@NotNull final PlayerJoinEvent event){
    final var manager = Provider.instance().profileManager();
    manager.process_of_join(event.getPlayer().getUniqueId());
  }

  @EventHandler
  public void onQuit(@NotNull final PlayerQuitEvent event){
    final var manager = Provider.instance().profileManager();
    manager.process_of_quit(event.getPlayer().getUniqueId());
  }

  @EventHandler
  public void onClickListener(InventoryClickEvent event){
    if (!(event.getWhoClicked() instanceof Player)) return;
    if (event.getClickedInventory() == null) return;
    if (event.getView().getTitle().equalsIgnoreCase("Dodge-Ball Games.")){
      event.setCancelled(true);
      if (event.getCurrentItem() == null) return;
      if (event.getCurrentItem().getItemMeta() == null) return;
      if (event.getCurrentItem().getItemMeta().getDisplayName().isEmpty()) return;

      final var name = event.getCurrentItem().getItemMeta().getDisplayName();
      final var manager = Provider.instance().gameManager();
      final var player = ((Player) event.getWhoClicked()).getPlayer();
      assert player != null;
      if (manager.join_room(name, player.getUniqueId())){
        System.out.println("ğağqğğq");
        event.getWhoClicked().closeInventory();
      }
    }
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    final var player = event.getPlayer();
    if (player.getInventory().getItemInMainHand().getType() == Material.PLAYER_HEAD) {
      player.getInventory().setItemInMainHand(null);
      final var eyeLocation = player.getEyeLocation();
      final var direction = eyeLocation.getDirection().normalize();
      final var armorStand = (ArmorStand) player.getWorld().spawnEntity(eyeLocation.add(direction), EntityType.ARMOR_STAND);
      armorStand.setVisible(false);
      armorStand.setSmall(true);
      armorStand.setBasePlate(false);
      armorStand.setGravity(true);

      final var helmet = createCustomSkull();
      final var helmetMeta = helmet.getItemMeta();
      helmet.setItemMeta(helmetMeta);

      armorStand.setHelmet(helmet);
      armorStand.setVelocity(direction.multiply(1.8));

      new BukkitRunnable() {
        @Override
        public void run() {
          if (armorStand.isOnGround()) {
            if (!armorStand.getNearbyEntities(1,1,1).isEmpty()){
              armorStand.getNearbyEntities(1,1,1).forEach(entity -> {
                if (entity instanceof Player){
                  Objects.requireNonNull(((Player) entity).getPlayer()).setGameMode(GameMode.SPECTATOR);
                  armorStand.remove();
                  player.getWorld().dropItem(armorStand.getLocation(), helmet);
                  player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0f, 1.0f);
                  player.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation(), 50, 0.2, 0.2, 0.2, 0.05);
                  cancel();
                }
              });
              return;
            }
            armorStand.remove();
            player.getWorld().dropItem(armorStand.getLocation(), helmet);
            player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0f, 1.0f);
            player.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation(), 50, 0.2, 0.2, 0.2, 0.05);
            cancel();
          }
        }
      }.runTaskTimer(instance, 0, 1);
    }
  }



  public static ItemStack createCustomSkull() {
    String base64Texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmM2NmRiZDhmYzM4MDU4ZTJjYmZkYjQ4OTQ2NDExMGUyYzI4Yjg1YjRhMDdlY2NhOWEwZjQwMmUyNTA5NDk4MyJ9fX0=";

    ItemStack skull = new ItemStack(Material.valueOf("PLAYER_HEAD"));
    SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

    GameProfile profile = new GameProfile(UUID.randomUUID(), null);
    profile.getProperties().put("textures", new Property("textures", base64Texture));

    Field profileField;
    try {
      profileField = skullMeta.getClass().getDeclaredField("profile");
      profileField.setAccessible(true);
      profileField.set(skullMeta, profile);
    } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
      e.printStackTrace();
    }

    skull.setItemMeta(skullMeta);

    return skull;
  }
}
