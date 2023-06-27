package com.beyazpoliss.bukkit;

import com.beyazpoliss.api.Provider;
import com.beyazpoliss.api.game.TeamType;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
public class PlayerCacheProcessor implements Listener {

  private final Plugin instance;

  private Set<UUID> cooldownPlayers;

  public PlayerCacheProcessor(DodgeBallPluginBukkit plugin) {
    this.instance = plugin;
    this.cooldownPlayers = new HashSet<>();
  }

  @EventHandler
  public void interact_team_selection(PlayerInteractEvent event) {
    final var player = event.getPlayer();
    if (player.getInventory().getItemInMainHand().getType() == Material.COMPASS) {
      openTeamSelectionMenu(player);
    }
  }

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
  public void team_selection(InventoryClickEvent event) {
    if (!(event.getWhoClicked() instanceof Player)) return;
    if (event.getClickedInventory() == null) return;
    if (event.getView().getTitle().equalsIgnoreCase("Team Selection.")){

      if (event.getCurrentItem() == null) return;
      if (event.getCurrentItem().getItemMeta() == null) return;
      if (event.getCurrentItem().getItemMeta().getDisplayName().isEmpty()) return;

      event.setCancelled(true);

      final var name = event.getCurrentItem().getItemMeta().getDisplayName();
      final var manager = Provider.instance().gameManager();
      final var player = ((Player) event.getWhoClicked()).getPlayer();
      assert player != null;
      player.playSound(player,Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
      if (name.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&',"&cRed Team"))){
        player.getInventory().clear();
        manager.change_team_player(player.getName(),player.getUniqueId(), TeamType.RED);
      } else if (name.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&',"&bBlue Team"))){
        player.getInventory().clear();
        manager.change_team_player(player.getName(),player.getUniqueId(), TeamType.BLUE);
      }
      player.closeInventory();
    }
  }

  private void openTeamSelectionMenu(Player player) {
    Inventory teamSelectionMenu = Bukkit.createInventory(null, 9, "Team Selection.");

    ItemStack redTeamItem = new ItemStack(Material.RED_WOOL);
    ItemMeta redTeamMeta = redTeamItem.getItemMeta();
    redTeamMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&cRed Team"));
    redTeamItem.setItemMeta(redTeamMeta);

    ItemStack blueTeamItem = new ItemStack(Material.BLUE_WOOL);
    ItemMeta blueTeamMeta = blueTeamItem.getItemMeta();
    blueTeamMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&bBlue Team"));
    blueTeamItem.setItemMeta(blueTeamMeta);

    teamSelectionMenu.setItem(3, redTeamItem);
    teamSelectionMenu.setItem(5, blueTeamItem);

    player.openInventory(teamSelectionMenu);
  }

  @EventHandler
  public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
    Player player = event.getPlayer();

    if (event.isSneaking() && !cooldownPlayers.contains(player.getUniqueId())) {
      Vector direction = player.getEyeLocation().getDirection();
      Vector dashVelocity = direction.multiply(1); // Hızlandırma faktörünü burada ayarlayabilirsiniz

      playDashSound(player);
      sendActionBarMessage(player, "Dash is being executed!");

      new BukkitRunnable() {
        int ticks = 0;
        int duration = 4; // Hızlandırma süresini burada ayarlayabilirsiniz

        @Override
        public void run() {
          player.setVelocity(dashVelocity);
          ticks++;
          if (ticks >= duration) {
            cancel();
          }
        }
      }.runTaskTimer(instance, 0, 1);

      cooldownPlayers.add(player.getUniqueId());
      startCooldown(player);
    } else {
      sendCooldownMessage(player);
    }
  }

  private void startCooldown(Player player) {
    new BukkitRunnable() {
      @Override
      public void run() {
        cooldownPlayers.remove(player.getUniqueId());
        sendActionBarMessage(player, "Cooldown is over!");
      }
    }.runTaskLater(instance, 25); // 3 saniye cooldown (60 tick olarak ayarlanmıştır)
  }

  private void playDashSound(Player player) {
    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f);
  }

  private void sendCooldownMessage(Player player) {
    sendActionBarMessage(player, "You are still on cooldown!");
  }

  private void sendActionBarMessage(Player player, String message) {
    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
  }


  @EventHandler
  public void onClickListener(InventoryClickEvent event){
    if (!(event.getWhoClicked() instanceof Player)) return;
    if (event.getClickedInventory() == null) return;
    if (event.getView().getTitle().equalsIgnoreCase("Dodge-Ball Games.")){

      if (event.getCurrentItem() == null) return;
      if (event.getCurrentItem().getItemMeta() == null) return;
      if (event.getCurrentItem().getItemMeta().getDisplayName().isEmpty()) return;

      event.setCancelled(true);

      final var name = event.getCurrentItem().getItemMeta().getDisplayName();
      final var manager = Provider.instance().gameManager();
      final var player = ((Player) event.getWhoClicked()).getPlayer();
      assert player != null;
      if (manager.join_room(name, player.getName(),player.getUniqueId())){
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
      final var armorStand = (ArmorStand) player.getWorld().spawnEntity(direction.clone().rotateAroundY(-10).normalize().toLocation(event.getPlayer().getWorld()), EntityType.ARMOR_STAND);
      armorStand.setVisible(false);
      armorStand.teleport(eyeLocation.add(direction));
      armorStand.setSmall(true);
      armorStand.setBasePlate(false);
      armorStand.setGravity(true);

      final var helmet = createCustomSkull();
      final var helmetMeta = helmet.getItemMeta();
      helmet.setItemMeta(helmetMeta);

      armorStand.setHelmet(helmet);
      armorStand.setVelocity(direction.multiply(2.5));
      final var manager = Provider.instance().gameManager();
      new BukkitRunnable() {
        @Override
        public void run() {
          if (armorStand.isOnGround()) {
            final var nearby = armorStand.getNearbyEntities(1,1,1);
            if (!nearby.isEmpty()) {

              for (Entity entity : nearby){
                if (entity instanceof final Player entityPlayer) {
                  if (player.canSee(entityPlayer)) {

                    final var entityRoom = manager.get_room_by_player(entityPlayer.getUniqueId());
                    final var damagerRoom = manager.get_room_by_player(player.getUniqueId());

                    if (entityRoom == null) continue;
                    if (damagerRoom == null) continue;

                    if (damagerRoom.players_is_enemy(player.getUniqueId(),entityPlayer.getUniqueId())){
                      Objects.requireNonNull(((Player) entity).getPlayer()).setGameMode(GameMode.SPECTATOR);
                      armorStand.remove();
                      player.getWorld().dropItem(armorStand.getLocation(), helmet);
                      player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0f, 1.0f);
                      player.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation(), 50, 0.2, 0.2, 0.2, 0.05);
                      cancel();
                    }
                  }
                }
              }
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

 private void sendEntityDestroyPacket(Player player, int entityId) {
    EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
    PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entityId);
    entityPlayer.b.a(packet);
  }
}
