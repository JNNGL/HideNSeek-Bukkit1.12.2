/*      */ package com.jnngl.plugin;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Random;

/*      */ import org.bukkit.Bukkit;
/*      */ import org.bukkit.ChatColor;
/*      */ import org.bukkit.Color;
/*      */ import org.bukkit.FireworkEffect;
/*      */ import org.bukkit.GameMode;
/*      */ import org.bukkit.Location;
/*      */ import org.bukkit.Material;
/*      */ import org.bukkit.World;
/*      */ import org.bukkit.block.Block;
/*      */ import org.bukkit.command.Command;
/*      */ import org.bukkit.command.CommandSender;
/*      */ import org.bukkit.command.ConsoleCommandSender;
/*      */ import org.bukkit.configuration.file.FileConfiguration;
/*      */ import org.bukkit.configuration.file.YamlConfiguration;
/*      */ import org.bukkit.entity.Firework;
/*      */ import org.bukkit.entity.Player;
/*      */ import org.bukkit.event.EventHandler;
/*      */ import org.bukkit.event.EventPriority;
/*      */ import org.bukkit.event.Listener;
/*      */ import org.bukkit.event.block.Action;
/*      */ import org.bukkit.event.entity.PlayerDeathEvent;
/*      */ import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
/*      */ import org.bukkit.event.player.PlayerRespawnEvent;
/*      */ import org.bukkit.inventory.Inventory;
/*      */ import org.bukkit.inventory.ItemStack;
/*      */ import org.bukkit.inventory.meta.FireworkMeta;
/*      */ import org.bukkit.plugin.java.JavaPlugin;
/*      */ import org.bukkit.potion.PotionEffect;
/*      */ import org.bukkit.potion.PotionEffectType;
/*      */ import org.bukkit.scheduler.BukkitRunnable;
/*      */ import org.bukkit.scoreboard.NameTagVisibility;
/*      */ import org.bukkit.scoreboard.Scoreboard;
/*      */ import org.bukkit.scoreboard.Team;

/*      */ 
/*      */ import com.jnngl.plugin.data.InventorySerializer;
/*      */ import com.jnngl.plugin.task.Timer;
/*      */ 
/*      */


/*      */ @SuppressWarnings({ "deprecation", "unchecked" })
public class Plugin
/*      */   extends JavaPlugin
/*      */   implements Listener
/*      */ {		
	
			
/*   51 */   ConsoleCommandSender console = getServer().getConsoleSender();
/*      */ 
/*      */   
/*      */   public boolean isValidKit(String kit) {
/*   55 */     String kits = getConfig().getString("kits");
/*      */     
/*   57 */     if (kits.toLowerCase().contains(", " + kit.toLowerCase())) {
/*   58 */       return true;
/*      */     }
/*   60 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public List<?> getListFromConfig(String path) {
/*   65 */     return getConfig().getList(path);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void onEnable() {
	
				//Bukkit.getPluginManager().getPlugin("PluginNameHere")
	
	
/*   71 */     getConfig().options().copyDefaults(true);
/*   72 */     saveConfig();
/*      */     
/*   74 */     createCustomConfig("data/playerData.yml");
/*      */     
/*   76 */     configurate(true);
/*      */     
/*   78 */     getServer().getPluginManager().registerEvents(this, this);
/*   79 */     getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Hide n Seek plugin enabled. Plugin by JNNGL.\n");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void earseConfig() {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void resetToDefaultSettings() {
/*   90 */     configurate(false);
/*      */   }
/*      */ 
/*      */   
/*      */   public void toggleSelection() {
/*   95 */     if (getConfig().getBoolean("selection")) {
/*   96 */       getConfig().set("selection", Boolean.valueOf(false));
/*      */     } else {
/*   98 */       getConfig().set("selection", Boolean.valueOf(true));
/*      */     } 
			   saveConfig();
/*      */   }
/*      */   
/*      */   public void enableSelection() {
/*  103 */     getConfig().set("selection", Boolean.valueOf(true));
		 	   saveConfig();
/*      */   }
/*      */ 
/*      */   
/*      */   public void disableSelection() {
/*  108 */     getConfig().set("selection", Boolean.valueOf(false));
			   saveConfig();
/*      */   }
/*      */   
/*  111 */   private List<ItemStack> kitItem = new ArrayList<>();
/*  112 */   private List<PotionEffect> kitEffect = new ArrayList<>();
/*      */   
/*  114 */   public static final String HNS = ChatColor.DARK_RED + "" + ChatColor.BOLD + "H'NS: " + ChatColor.RESET;
/*  115 */   public static final String INVALID_ARGS = String.valueOf(HNS) + ChatColor.RED + "Invalid usage of arguments"; private File customconfigFile;
/*      */   private FileConfiguration customconfig;
/*      */   
/*      */   public void configurate(boolean condition) {
/*  119 */     registerTeam();
/*  120 */     if (!condition || !getConfig().isSet("configurated") || !getConfig().getBoolean("configurated")) {
/*      */       
/*  122 */       getConfig().set("selection", Boolean.valueOf(true));
/*      */       
/*  124 */       getConfig().set("hub.x", Integer.valueOf(0));
/*  125 */       getConfig().set("hub.y", Integer.valueOf(0));
/*  126 */       getConfig().set("hub.z", Integer.valueOf(0));
/*      */       
/*  128 */       getConfig().set("seekWait.x", Integer.valueOf(0));
/*  129 */       getConfig().set("seekWait.y", Integer.valueOf(0));
/*  130 */       getConfig().set("seekWait.z", Integer.valueOf(0));
/*      */       
/*  132 */       getConfig().set("start.x", Integer.valueOf(0));
/*  133 */       getConfig().set("start.y", Integer.valueOf(0));
/*  134 */       getConfig().set("start.z", Integer.valueOf(0));
/*      */       
/*  136 */       getConfig().set("displayRoom", Boolean.valueOf(false));
/*  137 */       getConfig().set("displayRoomAt", Integer.valueOf(120));
/*  138 */       getConfig().set("roomList", new ArrayList<String>());
/*      */       
/*  140 */       getConfig().set("hideTime", Integer.valueOf(45));
/*  141 */       getConfig().set("seekTime", Integer.valueOf(240));
/*      */ 
/*      */ 
/*      */       
/*  145 */       getConfig().set("minPlayerCount", Integer.valueOf(2));
/*  146 */       getConfig().set("waitTimeBeforeStart", Integer.valueOf(60));
/*  147 */       getConfig().set("maxPlayerCount", Integer.valueOf(10));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  153 */       getConfig().set("world", "InsertHideAndSeekWorldHere");
/*      */       
/*  155 */       getConfig().set("messageAt.120", "&yellow120 seconds left!");
/*  156 */       getConfig().set("messageAt.40", "&red40 seconds left!");
/*      */       
/*  158 */       getConfig().set("enoughToPlay", Integer.valueOf(2));
/*      */       
/*  160 */       getConfig().set("kits", ", jumper, seeker");
/*  161 */       getConfig().set("defaultKit", "jumper");
/*      */       
/*  163 */       getConfig().set("message.waitingForPlayers", "&yellow{PLAYER} &aquajoined the game! {COUNT} players remaining!");
/*  164 */       getConfig().set("message.playerJoined", "&yellow{PLAYER} &aquajoined the game!");
/*  165 */       getConfig().set("message.waitingForGameStart", "&greenThe game will start in {AMOUNT} seconds!");
/*  166 */       getConfig().set("message.gameStarted", "&greenThe game started! The seeker is &yellow[{PLAYER}]!");
/*  167 */       getConfig().set("message.timeToHide", "&aquaHiders have {AMOUNT} seconds to hide!");
/*  168 */       getConfig().set("message.timeBeforeRelease", "&redSeeker(s) will be released in {AMOUNT} seconds!");
/*  169 */       getConfig().set("message.seekerReleased", "&greenThe seeker(s) has been released!");
/*  170 */       getConfig().set("message.hiderFound", "&redHider &yellow[{PLAYER}] &redwas found! {COUNT} hider(s) left!");
/*  171 */       getConfig().set("message.foundYourself", "&redSeeker &yellow[{PLAYER}] &redfound yourself!");
/*  172 */       getConfig().set("message.seekerFound", "&redSeeker &yellow[{PLAYER}] &redwas found... Oops!");
/*  173 */       getConfig().set("message.gaveUpKill", "&redHider &yellow[{PLAYER}] &redgave up by killing yourself! {COUNT} hider(s) left!");
/*  174 */       getConfig().set("message.gaveUpLeft", "&redHider &yellow[{PLAYER}] &redgave up and left the game!");
/*  175 */       getConfig().set("message.seekerLeftNew", "&redSeeker &yellow[{PLAYER}] &redgave up and left the game! &yellow[{SEEKER}] &redis seeker now!");
/*  176 */       getConfig().set("message.notEnough", "&redGame stopped! Not enough players");
/*  177 */       getConfig().set("message.seekerLeft", "&redSeeker &yellow[{PLAYER}] &redgave up and left the game!");
/*  178 */       getConfig().set("message.hidersWin", "&greenGame over! Hiders win!");
/*  179 */       getConfig().set("message.seekersWin", "&greenGame over! Seekers win!");
/*  180 */       getConfig().set("message.tpback", "&aquaYou will be teleported back in {SECONDS} seconds!");
/*  181 */       getConfig().set("message.kit", "&l_purpleType &yellow/kitlist &l_purpleto get kit list; Type &yellow/getkit [kit] &l_purpleto get kit; If you want to leave game type &yellow/leaveHideAndSeek&l_purple.");
/*  182 */       getConfig().set("message.left", "&yellow{PLAYER} &aqualeft the game!");
/*  183 */       getConfig().set("message.seekerhave", "&greenSeeker have {SECONDS} seconds to find all hiders!");
/*  184 */       getConfig().set("message.onlyLeft", "&redOnly {SECONDS} seconds left!");
/*  185 */       getConfig().set("message.inRoom", "&aquaHider &yellow[{PLAYER}] &aqua in the &yellow[{ROOM}] &aquaroom!");
/*      */       
/*  187 */       getConfig().set("configurated", Boolean.valueOf(true));
/*  188 */       saveConfig();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void clearPotionEffects(Player p) {
/*  194 */     for (PotionEffect effect : p.getActivePotionEffects()) {
/*  195 */       p.removePotionEffect(effect.getType());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void onDisable() {
/*  201 */     this.console.sendMessage(ChatColor.GREEN + "Hide n Seek plugin disabled.\n");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
/*  209 */     Player p = null;
/*  210 */     Location loc = null;
/*      */     
/*  212 */     if (sender instanceof Player) {
/*      */       
/*  214 */       p = (Player)sender;
/*  215 */       loc = p.getLocation();
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  220 */     if (sender instanceof Player) {
/*      */       
/*  222 */       if (label.equalsIgnoreCase("kitList")) {
/*      */         
/*  224 */         if (getConfig().getString("kits").length() > 2) {
/*  225 */           sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "Kits: " + ChatColor.AQUA + getConfig().getString("kits").substring(2).replace(", seeker", "").replace("seeker", ""));
/*      */         } else {
/*  227 */           sender.sendMessage(String.valueOf(HNS) + ChatColor.RED + "There is no kits!");
/*  228 */         }  return true;
/*      */       } 
/*      */       
/*  231 */       if (label.equalsIgnoreCase("getKit")) {
/*      */         
/*  233 */         if (args.length == 1) {
/*      */           
/*  235 */           if (args[0].equals("seeker")) {
/*      */             
/*  237 */             sender.sendMessage(String.valueOf(HNS) + ChatColor.RED + "You can't use seeker kit!");
/*  238 */             return true;
/*      */           } 
/*      */           
/*  241 */           if (this.isStart) {
/*      */             
/*  243 */             sender.sendMessage(String.valueOf(HNS) + ChatColor.RED + "You can't change your kit!");
/*  244 */             return true;
/*      */           } 
/*      */           
/*  247 */           if (!this.players.contains(p)) {
/*      */             
/*  249 */             sender.sendMessage(String.valueOf(HNS) + ChatColor.RED + "You can use kit only in hide n Seek hub!");
/*  250 */             return true;
/*      */           } 
/*      */           
/*  253 */           if (isValidKit(args[0])) {
/*      */ 
/*      */             
/*  256 */             List<ItemStack> items = (List<ItemStack>)getListFromConfig("kit." + args[0] + ".items");
/*  257 */             List<PotionEffect> effects = (List<PotionEffect>)getListFromConfig("kit." + args[0] + ".effects");
/*      */             
/*  259 */             p.getInventory().clear();
/*  260 */             clearPotionEffects(p);
/*      */             
/*  262 */             for (ItemStack item : items) {
/*      */               
/*  264 */               p.getInventory().addItem(new ItemStack[] { item });
/*      */             } 
/*  266 */             for (PotionEffect effect : effects)
/*      */             {
/*  268 */               p.addPotionEffect(effect, false);
/*      */             }
/*      */             
/*  271 */             sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "Kit '" + args[0] + "' successfully gived to you!");
/*  272 */             this.kitted.add(p);
/*      */           } else {
/*      */             
/*  275 */             sender.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Invalid kit!");
/*      */           } 
/*      */         } else {
/*  278 */           sender.sendMessage(INVALID_ARGS);
/*      */         } 
/*  280 */         return true;
/*      */       } 
/*  282 */       if (label.equalsIgnoreCase("hideandseek")) {
/*      */         
/*  284 */         joinToGame(p);
/*  285 */         return true;
/*      */       } 
/*  287 */       if (label.equalsIgnoreCase("leavehideandseek")) {
/*      */         
/*  289 */         leftGame(p);
/*  290 */         return true;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  299 */     if (p != null && !p.isOp()) {
/*      */       
/*  301 */       sender.sendMessage(String.valueOf(HNS) + ChatColor.RED + "You don't have enough permissions.");
/*  302 */       return true;
/*      */     } 
/*      */     
/*  305 */     if (sender instanceof Player) {
/*      */ 
/*      */       
/*  308 */       if (label.equalsIgnoreCase("addKitElement")) {
/*      */ 
/*      */         
/*  311 */         if (args.length == 1) {
/*      */           
/*  313 */           if (args[0].equalsIgnoreCase("item")) {
/*      */             
/*  315 */             ItemStack item = p.getItemInHand();
/*  316 */             this.kitItem.add(item);
/*  317 */             sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "Item successfully added! Here is it: Type: " + item.getTypeId() + ", Amount: " + item.getAmount() + ", Durability: " + item.getDurability() + ". (Enchantments not displayed)");
/*      */           } else {
/*      */             
/*  320 */             sender.sendMessage(INVALID_ARGS);
/*  321 */           }  return true;
/*      */         } 
/*  323 */         if (args.length == 4) {
/*      */           
/*  325 */           if (args[0].equalsIgnoreCase("effect")) {
/*      */             
/*  327 */             PotionEffectType pet = PotionEffectType.getById(Integer.parseInt(args[1]));
/*      */             
/*  329 */             PotionEffect effect = new PotionEffect(pet, Integer.parseInt(args[2]), Integer.parseInt(args[3]));
/*      */             
/*  331 */             this.kitEffect.add(effect);
/*  332 */             sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "Effect successfully added! Here is it: Type: " + effect.getType().getName() + "(" + effect.getType().getId() + "), Duration: " + effect.getDuration() + ", Amplifier: " + effect.getAmplifier() + ".");
/*      */           } else {
/*      */             
/*  335 */             sender.sendMessage(INVALID_ARGS);
/*      */           } 
/*  337 */           return true;
/*      */         } 
/*      */ 
/*      */         
/*  341 */         sender.sendMessage(INVALID_ARGS);
/*  342 */         return true;
/*      */       } 
/*      */ 
/*      */       
/*  346 */       if (label.equalsIgnoreCase("removeKitElement")) {
/*      */         
/*  348 */         if (args.length == 1) {
/*      */           
/*  350 */           if (args[0].equalsIgnoreCase("item")) {
/*      */             
/*  352 */             ItemStack item = p.getItemInHand();
/*  353 */             this.kitItem.remove(item);
/*  354 */             sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "Item successfully removed!");
/*      */           } else {
/*      */             
/*  357 */             sender.sendMessage(INVALID_ARGS);
/*  358 */           }  return true;
/*      */         } 
/*  360 */         if (args.length == 4) {
/*      */           
/*  362 */           if (args[0].equalsIgnoreCase("effect")) {
/*      */             
/*  364 */             PotionEffectType pet = null;
/*      */             
/*  366 */             pet = PotionEffectType.getByName(args[1]);
/*      */             
/*  368 */             if (pet.equals(null)) {
/*  369 */               PotionEffectType.getById(Integer.parseInt(args[0]));
/*      */             }
/*  371 */             if (pet.equals(null)) {
/*      */               
/*  373 */               sender.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Invalid effect type!");
/*  374 */               return true;
/*      */             } 
/*      */             
/*  377 */             PotionEffect effect = new PotionEffect(pet, Integer.parseInt(args[2]), Integer.parseInt(args[3]), true, false);
/*      */             
/*  379 */             if (effect.equals(null)) {
/*      */               
/*  381 */               sender.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Invalid effect!");
/*  382 */               return true;
/*      */             } 
/*      */             
/*  385 */             this.kitEffect.remove(effect);
/*  386 */             sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "Effect successfully removed!");
/*      */           } else {
/*      */             
/*  389 */             sender.sendMessage(INVALID_ARGS);
/*      */           } 
/*  391 */           return true;
/*      */         } 
/*      */ 
/*      */         
/*  395 */         sender.sendMessage(INVALID_ARGS);
/*  396 */         return true;
/*      */       } 
/*      */ 
/*      */       
/*  400 */       if (label.equalsIgnoreCase("applyKit")) {
/*      */         
/*  402 */         if (args.length == 1) {
/*      */           
/*  404 */           if (args[0].equals("seeker")) {
/*      */             
/*  406 */             sender.sendMessage(String.valueOf(HNS) + ChatColor.RED + "You can't change seeker kit using in-game commands!");
/*  407 */             return true;
/*      */           } 
/*      */           
/*  410 */           if (!getConfig().getString("kits").toLowerCase().contains(", " + args[0].toLowerCase())) {
/*      */             
/*  412 */             String kits = getConfig().getString("kits");
/*  413 */             getConfig().set("kits", String.valueOf(kits) + ", " + args[0]);
/*      */           } 
/*  415 */           getConfig().set("kit." + args[0] + ".items", this.kitItem);
/*  416 */           getConfig().set("kit." + args[0] + ".effects", this.kitEffect);
/*  417 */           saveConfig();
/*      */           
/*  419 */           this.kitItem = new ArrayList<>();
/*  420 */           this.kitEffect = new ArrayList<>();
/*      */           
/*  422 */           sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "Kit with name '" + args[0] + "' successfully created/edited!");
/*  423 */           return true;
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  429 */         sender.sendMessage(INVALID_ARGS);
/*      */         
/*  431 */         return true;
/*      */       } 
/*  433 */       if (label.equalsIgnoreCase("editKit")) {
/*      */         
/*  435 */         if (args.length == 1) {
/*      */           
/*  437 */           if (args[0].equals("seeker")) {
/*      */             
/*  439 */             sender.sendMessage(String.valueOf(HNS) + ChatColor.RED + "You can't change seeker kit using in-game commands!");
/*  440 */             return true;
/*      */           } 
/*      */           
/*  443 */           if (isValidKit(args[0])) {
/*      */             
/*  445 */             this.kitItem = (List<ItemStack>)getListFromConfig("kit." + args[0] + ".items");
/*  446 */             this.kitEffect = (List<PotionEffect>)getListFromConfig("kit." + args[0] + ".effects");
/*      */             
/*  448 */             sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "Kit successfully loaded. Now you can edit it using /addKitElement, /removeKitElement and /clearKit commands.");
/*      */           } else {
/*      */             
/*  451 */             sender.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Invalid kit!");
/*      */           } 
/*      */         } else {
/*  454 */           sender.sendMessage(INVALID_ARGS);
/*  455 */         }  return true;
/*      */       } 
/*  457 */       if (label.equalsIgnoreCase("deleteKit")) {
/*      */         
/*  459 */         if (args.length == 1) {
/*      */           
/*  461 */           if (args[0].equals("seeker")) {
/*      */             
/*  463 */             sender.sendMessage(String.valueOf(HNS) + ChatColor.RED + "You can't change seeker kit using in-game commands!");
/*  464 */             return true;
/*      */           } 
/*      */           
/*  467 */           if (isValidKit(args[0])) {
/*      */             
/*  469 */             String kits = getConfig().getString("kits");
/*  470 */             kits = kits.replace(args[0], "");
/*  471 */             getConfig().set("kits", kits);
/*  472 */             getConfig().set("kit." + args[0] + ".items", null);
/*  473 */             getConfig().set("kit." + args[0] + ".effects", null);
/*  474 */             getConfig().set("kit." + args[0], null);
/*      */             
/*  476 */             saveConfig();
/*      */             
/*  478 */             sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "Kit successfully deleted!");
/*      */           }
/*      */           else {
/*      */             
/*  482 */             sender.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Invalid kit!");
/*      */           } 
/*      */         } else {
/*  485 */           sender.sendMessage(INVALID_ARGS);
/*      */         } 
/*  487 */         return true;
/*      */       } 
/*  489 */       if (label.equalsIgnoreCase("setDefaultKit")) {
/*      */         
/*  491 */         if (args[0].equals("seeker")) {
/*      */           
/*  493 */           sender.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Seeker can't be used as default kit!");
/*  494 */           return true;
/*      */         } 
/*      */         
/*  497 */         if (args.length == 1) {
/*      */           
/*  499 */           if (isValidKit(args[0])) {
/*      */             
/*  501 */             getConfig().set("defaultKit", args[0]);
/*  502 */             saveConfig();
/*      */             
/*  504 */             sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "Kit '" + args[0] + "' successfully changed to default!");
/*      */           } else {
/*      */             
/*  507 */             sender.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Invalid kit!");
/*      */           } 
/*      */         } else {
/*  510 */           sender.sendMessage(INVALID_ARGS);
/*      */         } 
/*  512 */         return true;
/*      */       } 
/*  514 */       if (label.equalsIgnoreCase("clearKit")) {
/*      */         
/*  516 */         this.kitItem = new ArrayList<>();
/*  517 */         this.kitEffect = new ArrayList<>();
/*      */         
/*  519 */         sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "Kit successfully cleared!");
/*  520 */         return true;
/*      */       } 
/*  522 */       if (label.equalsIgnoreCase("saveData")) {
/*      */         
/*  524 */         if (args.length > 1) {
/*      */           
/*  526 */           savePlayerData(Bukkit.getPlayer(args[0]));
/*  527 */           sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + Bukkit.getPlayer(args[0]) + "'s data successfully saved!");
/*      */           
/*  529 */           return true;
/*      */         } 
/*  531 */         savePlayerData(p);
/*  532 */         sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "Data successfully saved!");
/*  533 */         return true;
/*      */       } 
/*  535 */       if (label.equalsIgnoreCase("loadData")) {
/*      */         
/*  537 */         if (args.length > 1) {
/*      */           
/*  539 */           loadPlayerData(Bukkit.getPlayer(args[0]));
/*  540 */           sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + Bukkit.getPlayer(args[0]) + "'s data successfully loaded!");
/*      */           
/*  542 */           return true;
/*      */         } 
/*      */         
/*  545 */         loadPlayerData(p);
/*  546 */         sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "Data successfully loaded!");
/*  547 */         return true;
/*      */       } 
/*  549 */       if (label.equalsIgnoreCase("tphub")) {
/*      */         
/*  551 */         if (teleportToHub(p))
/*  552 */           sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "You've been teleported to hub"); 
/*  553 */         return true;
/*      */       } 
/*  555 */       if (label.equalsIgnoreCase("tpseekroom")) {
/*      */         
/*  557 */         if (teleportToSeekerRoom(p))
/*  558 */           sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "You've been teleported to seeker room"); 
/*  559 */         return true;
/*      */       } 
/*  561 */       if (label.equalsIgnoreCase("tpstart")) {
/*      */         
/*  563 */         if (teleportToStart(p))
/*  564 */           sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "You've been teleported to map start"); 
/*  565 */         return true;
/*      */       } 
/*  567 */       if (label.equalsIgnoreCase("setroom")) {
/*      */         
/*  569 */         if (isRegionSelected(p)) {
/*      */           
/*  571 */           if (args.length == 1) {
/*      */             
/*  573 */             Location fl = this.firstPos.get(p);
/*  574 */             Location sl = this.secondPos.get(p);
/*      */             
/*  576 */             int fx = fl.getBlockX();
/*  577 */             int fy = fl.getBlockY();
/*  578 */             int fz = fl.getBlockZ();
/*      */             
/*  580 */             int sx = sl.getBlockX();
/*  581 */             int sy = sl.getBlockY();
/*  582 */             int sz = sl.getBlockZ();
/*      */             
/*  584 */             String world = fl.getWorld().getName();
/*      */             
/*  586 */             getConfig().set("rooms." + args[0] + ".first.x", Integer.valueOf(fx));
/*  587 */             getConfig().set("rooms." + args[0] + ".first.y", Integer.valueOf(fy));
/*  588 */             getConfig().set("rooms." + args[0] + ".first.z", Integer.valueOf(fz));
/*      */             
/*  590 */             getConfig().set("rooms." + args[0] + ".second.x", Integer.valueOf(sx));
/*  591 */             getConfig().set("rooms." + args[0] + ".second.y", Integer.valueOf(sy));
/*  592 */             getConfig().set("rooms." + args[0] + ".second.z", Integer.valueOf(sz));
/*      */             
/*  594 */             getConfig().set("rooms." + args[0] + ".world", world);
/*      */             
/*  596 */             registerRoom(args[0]);
/*      */             
/*  598 */             saveConfig();
/*  599 */             sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "Room '" + args[0] + "' successfully binded!");
/*  600 */             return true;
/*      */           } 
/*      */           
/*  603 */           sender.sendMessage(INVALID_ARGS);
/*      */         } else {
/*      */           
/*  606 */           sender.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Select region using iron axe first!");
/*      */         } 
/*  608 */         return true;
/*      */       } 
/*  610 */       if (label.equalsIgnoreCase("delroom")) {
/*      */         
/*  612 */         if (args.length == 1) {
/*      */           
/*  614 */           if (getConfig().isSet("rooms." + args[0])) {
/*      */             
/*  616 */             unregisterRoom(args[0]);
/*  617 */             getConfig().set("rooms." + args[0], null);
/*  618 */             saveConfig();
/*  619 */             sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "Room '" + args[0] + "' successfully deleted!");
/*      */           } else {
/*      */             
/*  622 */             sender.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Room not found!");
/*      */           } 
/*      */         } else {
/*  625 */           sender.sendMessage(INVALID_ARGS);
/*      */         } 
/*  627 */         return true;
/*      */       } 
/*  629 */       if (label.equalsIgnoreCase("getRoom")) {
/*      */         
/*  631 */         String room = getRoom(p);
/*      */         
/*  633 */         if (room != null) {
/*      */           
/*  635 */           sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "You are in a '" + room + "' room");
/*      */         } else {
/*      */           
/*  638 */           sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "You are in a room that doesn't exist!");
/*      */         } 
/*  640 */         return true;
/*      */       } 
/*  642 */       if (label.equalsIgnoreCase("stopgame")) {
/*      */         
/*  644 */         stopGame();
/*  645 */         p.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "Game has been stopped!");
/*  646 */         return true;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  652 */     if (label.equalsIgnoreCase("hnsReset")) {
/*      */       
/*  654 */       resetToDefaultSettings();
/*  655 */       sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "HNS config file successfully updated!");
/*  656 */       return true;
/*      */     } 
/*  658 */     if (label.equalsIgnoreCase("setupHubRoom")) {
/*      */       
/*  660 */       if (args.length == 3) {
/*      */         
/*  662 */         int x = Integer.parseInt(args[0]);
/*  663 */         int y = Integer.parseInt(args[1]);
/*  664 */         int z = Integer.parseInt(args[2]);
/*      */         
/*  666 */         if (x == 0 && y == 0 && z == 0) {
/*      */           
/*  668 */           sender.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Invalid Integers: all integers can't be 0; Please use valid integers");
/*  669 */           return true;
/*      */         } 
/*      */         
/*  672 */         getConfig().set("hub.x", Integer.valueOf(x));
/*  673 */         getConfig().set("hub.y", Integer.valueOf(y));
/*  674 */         getConfig().set("hub.z", Integer.valueOf(z));
/*  675 */         saveConfig();
/*  676 */         sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "Hub room successfully binded to: X: " + x + ", Y:" + y + ", Z" + z + ".");
/*  677 */         return true;
/*      */       } 
/*  679 */       if (args.length != 3 && sender instanceof Player) {
/*      */         
/*  681 */         double x = loc.getX();
/*  682 */         double y = loc.getY();
/*  683 */         double z = loc.getZ();
/*      */         
/*  685 */         if (x == 0.0D && y == 0.0D && z == 0.0D) {
/*      */           
/*  687 */           sender.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Invalid Integers: all integers can't be 0; Please use valid integers");
/*  688 */           return true;
/*      */         } 
/*      */         
/*  691 */         getConfig().set("hub.x", Double.valueOf(x));
/*  692 */         getConfig().set("hub.y", Double.valueOf(y));
/*  693 */         getConfig().set("hub.z", Double.valueOf(z));
/*  694 */         saveConfig();
/*  695 */         sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "Hub room successfully binded to: X: " + x + ", Y: " + y + ", Z: " + z + ".");
/*  696 */         return true;
/*      */       } 
/*      */ 
/*      */       
/*  700 */       sender.sendMessage(INVALID_ARGS);
/*  701 */       return true;
/*      */     } 
/*      */     
/*  704 */     if (label.equalsIgnoreCase("setupSeekerRoom")) {
/*      */       
/*  706 */       if (args.length == 3) {
/*      */         
/*  708 */         int x = Integer.parseInt(args[0]);
/*  709 */         int y = Integer.parseInt(args[1]);
/*  710 */         int z = Integer.parseInt(args[2]);
/*      */         
/*  712 */         if (x == 0 && y == 0 && z == 0) {
/*      */           
/*  714 */           sender.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Invalid Integers: all integers can't be 0; Please use valid integers");
/*  715 */           return true;
/*      */         } 
/*      */         
/*  718 */         getConfig().set("seekWait.x", Integer.valueOf(x));
/*  719 */         getConfig().set("seekWait.y", Integer.valueOf(y));
/*  720 */         getConfig().set("seekWait.z", Integer.valueOf(z));
/*  721 */         saveConfig();
/*  722 */         sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "Seeker room successfully binded to: X: " + x + ", Y:" + y + ", Z" + z + ".");
/*  723 */         return true;
/*      */       } 
/*  725 */       if (args.length != 3 && sender instanceof Player) {
/*      */         
/*  727 */         double x = loc.getX();
/*  728 */         double y = loc.getY();
/*  729 */         double z = loc.getZ();
/*      */         
/*  731 */         if (x == 0.0D && y == 0.0D && z == 0.0D) {
/*      */           
/*  733 */           sender.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Invalid Integers: all integers can't be 0; Please use valid integers");
/*  734 */           return true;
/*      */         } 
/*      */         
/*  737 */         getConfig().set("seekWait.x", Double.valueOf(x));
/*  738 */         getConfig().set("seekWait.y", Double.valueOf(y));
/*  739 */         getConfig().set("seekWait.z", Double.valueOf(z));
/*  740 */         saveConfig();
/*  741 */         sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "Seeker room successfully binded to: X: " + x + ", Y: " + y + ", Z: " + z + ".");
/*  742 */         return true;
/*      */       } 
/*      */ 
/*      */       
/*  746 */       sender.sendMessage(INVALID_ARGS);
/*  747 */       return true;
/*      */     } 
/*      */     
/*  750 */     if (label.equalsIgnoreCase("setupStart")) {
/*      */       
/*  752 */       if (args.length == 3) {
/*      */         
/*  754 */         int x = Integer.parseInt(args[0]);
/*  755 */         int y = Integer.parseInt(args[1]);
/*  756 */         int z = Integer.parseInt(args[2]);
/*      */         
/*  758 */         if (x == 0 && y == 0 && z == 0) {
/*      */           
/*  760 */           sender.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Invalid Integers: all integers can't be 0; Please use valid integers");
/*  761 */           return true;
/*      */         } 
/*      */         
/*  764 */         getConfig().set("start.x", Integer.valueOf(x));
/*  765 */         getConfig().set("start.y", Integer.valueOf(y));
/*  766 */         getConfig().set("start.z", Integer.valueOf(z));
/*  767 */         saveConfig();
/*  768 */         sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "Start position successfully binded to: X: " + x + ", Y:" + y + ", Z" + z + ".");
/*  769 */         return true;
/*      */       } 
/*  771 */       if (args.length != 3 && sender instanceof Player) {
/*      */         
/*  773 */         double x = loc.getX();
/*  774 */         double y = loc.getY();
/*  775 */         double z = loc.getZ();
/*      */         
/*  777 */         if (x == 0.0D && y == 0.0D && z == 0.0D) {
/*      */           
/*  779 */           sender.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Invalid Integers: all integers can't be 0; Please use valid integers");
/*  780 */           return true;
/*      */         } 
/*      */         
/*  783 */         getConfig().set("start.x", Double.valueOf(x));
/*  784 */         getConfig().set("start.y", Double.valueOf(y));
/*  785 */         getConfig().set("start.z", Double.valueOf(z));
/*  786 */         saveConfig();
/*  787 */         sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "Start position successfully binded to: X: " + x + ", Y: " + y + ", Z: " + z + ".");
/*  788 */         return true;
/*      */       } 
/*      */ 
/*      */       
/*  792 */       sender.sendMessage(INVALID_ARGS);
/*  793 */       return true;
/*      */     } 
/*      */     
/*  796 */     if (label.equalsIgnoreCase("setDisplayRoom")) {
/*      */       
/*  798 */       if (args.length == 1) {
/*      */ 
/*      */         
/*  801 */         getConfig().set("displayRoom", Boolean.valueOf(Boolean.parseBoolean(args[0])));
/*  802 */         saveConfig();
/*  803 */         sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "HNS config file successfully updated!");
/*      */       } else {
/*      */         
/*  806 */         sender.sendMessage(INVALID_ARGS);
/*  807 */       }  return true;
/*      */     } 
/*  809 */     if (label.equalsIgnoreCase("setDisplayRoomAt")) {
/*      */       
/*  811 */       if (args.length == 1) {
/*      */ 
/*      */         
/*  814 */         getConfig().set("displayRoomAt", Integer.valueOf(Integer.parseInt(args[0])));
/*  815 */         saveConfig();
/*  816 */         sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "HNS config file successfully updated!");
/*      */       } else {
/*      */         
/*  819 */         sender.sendMessage(INVALID_ARGS);
/*  820 */       }  return true;
/*      */     } 
/*  822 */     if (label.equalsIgnoreCase("setHideTime")) {
/*      */       
/*  824 */       if (args.length == 1) {
/*      */ 
/*      */         
/*  827 */         getConfig().set("hideTime", Integer.valueOf(Integer.parseInt(args[0])));
/*  828 */         saveConfig();
/*  829 */         sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "HNS config file successfully updated!");
/*      */       } else {
/*      */         
/*  832 */         sender.sendMessage(INVALID_ARGS);
/*  833 */       }  return true;
/*      */     } 
/*  835 */     if (label.equalsIgnoreCase("setSeekTime")) {
/*      */       
/*  837 */       if (args.length == 1) {
/*      */ 
/*      */         
/*  840 */         getConfig().set("seekTime", Integer.valueOf(Integer.parseInt(args[0])));
/*  841 */         saveConfig();
/*  842 */         sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "HNS config file successfully updated!");
/*      */       } else {
/*      */         
/*  845 */         sender.sendMessage(INVALID_ARGS);
/*  846 */       }  return true;
/*      */     } 
/*  848 */     if (label.equalsIgnoreCase("setMinPlayerCount")) {
/*      */       
/*  850 */       if (args.length == 1) {
/*      */ 
/*      */         
/*  853 */         getConfig().set("minPlayerCount", Integer.valueOf(Integer.parseInt(args[0])));
/*  854 */         saveConfig();
/*  855 */         sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "HNS config file successfully updated!");
/*      */       } else {
/*      */         
/*  858 */         sender.sendMessage(INVALID_ARGS);
/*  859 */       }  return true;
/*      */     } 
/*  861 */     if (label.equalsIgnoreCase("setWaitTimeBeforeStart")) {
/*      */       
/*  863 */       if (args.length == 1) {
/*      */ 
/*      */         
/*  866 */         getConfig().set("waitTimeBeforeStart", Integer.valueOf(Integer.parseInt(args[0])));
/*  867 */         saveConfig();
/*  868 */         sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "HNS config file successfully updated!");
/*      */       } else {
/*      */         
/*  871 */         sender.sendMessage(INVALID_ARGS);
/*  872 */       }  return true;
/*      */     } 
/*  874 */     if (label.equalsIgnoreCase("setMaxPlayerCount")) {
/*      */       
/*  876 */       if (args.length == 1) {
/*      */ 
/*      */         
/*  879 */         getConfig().set("maxPlayerCount", Integer.valueOf(Integer.parseInt(args[0])));
/*  880 */         saveConfig();
/*  881 */         sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "HNS config file successfully updated!");
/*      */       } else {
/*      */         
/*  884 */         sender.sendMessage(INVALID_ARGS);
/*  885 */       }  return true;
/*      */     } 
/*  887 */     if (label.equalsIgnoreCase("setSeekersAtStart")) {
/*      */       
/*  889 */       if (args.length == 1) {
/*      */ 
/*      */         
/*  892 */         getConfig().set("seekersAtStart", Integer.valueOf(Integer.parseInt(args[0])));
/*  893 */         saveConfig();
/*  894 */         sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "HNS config file successfully updated!");
/*      */       } else {
/*      */         
/*  897 */         sender.sendMessage(INVALID_ARGS);
/*  898 */       }  return true;
/*      */     } 
/*  900 */     if (label.equalsIgnoreCase("setSeekersBoundAtStart")) {
/*      */       
/*  902 */       if (args.length == 1) {
/*      */ 
/*      */         
/*  905 */         getConfig().set("seekersBoundAtStart", Integer.valueOf(Integer.parseInt(args[0])));
/*  906 */         saveConfig();
/*  907 */         sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "HNS config file successfully updated!");
/*      */       } else {
/*      */         
/*  910 */         sender.sendMessage(INVALID_ARGS);
/*  911 */       }  return true;
/*      */     } 
/*  913 */     if (label.equalsIgnoreCase("setWorld")) {
/*      */       
/*  915 */       if (args.length == 1) {
/*      */ 
/*      */         
/*  918 */         getConfig().set("world", args[0]);
/*  919 */         saveConfig();
/*  920 */         sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "HNS config file successfully updated!");
/*      */       } else {
/*      */         
/*  923 */         sender.sendMessage(INVALID_ARGS);
/*  924 */       }  return true;
/*      */     } 
/*  926 */     if (label.equalsIgnoreCase("addMessageAt")) {
/*      */       
/*  928 */       if (args.length > 1) {
/*      */         
/*  930 */         String msg = "";
/*  931 */         for (int i = 1; i < args.length; i++)
/*  932 */           msg = String.valueOf(msg) + args[i] + " "; 
/*  933 */         getConfig().set("messageAt." + Integer.parseInt(args[0]), msg);
/*  934 */         saveConfig();
/*  935 */         sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "HNS config file successfully updated!");
/*      */       } else {
/*      */         
/*  938 */         sender.sendMessage(INVALID_ARGS);
/*  939 */       }  return true;
/*      */     } 
/*  941 */     if (label.equalsIgnoreCase("removeMessageAt")) {
/*      */       
/*  943 */       if (args.length == 1) {
/*      */ 
/*      */         
/*  946 */         getConfig().set("messageAt." + Integer.parseInt(args[0]), null);
/*  947 */         saveConfig();
/*  948 */         sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "HNS config file successfully updated!");
/*      */       } else {
/*      */         
/*  951 */         sender.sendMessage(INVALID_ARGS);
/*  952 */       }  return true;
/*      */     } 
/*  954 */     if (label.equalsIgnoreCase("setEnoughToPlay")) {
/*      */       
/*  956 */       if (args.length == 1) {
/*      */ 
/*      */         
/*  959 */         getConfig().set("enoughToPlay", Integer.valueOf(Integer.parseInt(args[0])));
/*  960 */         saveConfig();
/*  961 */         sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "HNS config file successfully updated!");
/*      */       } else {
/*      */         
/*  964 */         sender.sendMessage(INVALID_ARGS);
/*  965 */       }  return true;
/*      */     } 
/*  967 */     if (label.equalsIgnoreCase("setMessage")) {
/*      */       
/*  969 */       if (args.length > 1) {
/*      */         
/*  971 */         String msg = "";
/*  972 */         for (int i = 1; i < args.length; i++) {
/*  973 */           msg = String.valueOf(msg) + args[i] + " ";
/*      */         }
/*  975 */         getConfig().set("message." + args[0], msg);
/*  976 */         saveConfig();
/*  977 */         sender.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "HNS config file successfully updated!");
/*      */       } else {
/*      */         
/*  980 */         sender.sendMessage(INVALID_ARGS);
/*  981 */       }  return true;
/*      */     } 
/*  983 */     if (label.equalsIgnoreCase("loadAllData")) {
/*      */       
/*  985 */       for (Player pp : Bukkit.getOnlinePlayers()) {
/*      */         
/*  987 */         if (getCustomConfig().isSet(p.getDisplayName())) {
/*  988 */           loadPlayerData(pp);
/*      */         }
/*      */       } 
/*  991 */       return true;
/*      */     } 
/*  993 */     if (label.equalsIgnoreCase("enableSelection")) {
/*      */       
/*  995 */       enableSelection();
/*  996 */       p.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "Area selection with golden pickaxe enabled!");
/*  997 */       return true;
/*      */     } 
/*  999 */     if (label.equalsIgnoreCase("disableSelection")) {
/*      */       
/* 1001 */       disableSelection();
/* 1002 */       p.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "Area selection with golden pickaxe disabled!");
/* 1003 */       return true;
/*      */     } 
/* 1005 */     if (label.equalsIgnoreCase("toggleSelection")) {
/*      */       
/* 1007 */       toggleSelection();
/* 1008 */       p.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "Area selection with golden pickaxe toggled to: " + getConfig().getBoolean("selection") + "!");
/* 1009 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 1013 */     sender.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Only players can use this command!");
/*      */ 
/*      */     
/* 1016 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FileConfiguration getCustomConfig() {
/* 1026 */     return this.customconfig;
/*      */   }
/*      */ 
/*      */   
/*      */   public void createCustomConfig(String path) {
/* 1031 */     this.customconfigFile = new File(getDataFolder(), "data/playerData.yml");
/* 1032 */     if (!this.customconfigFile.exists()) {
/*      */       
/* 1034 */       this.customconfigFile.getParentFile().mkdirs();
/* 1035 */       saveResource(path, false);
/*      */     } 
/*      */     
/* 1038 */     this.customconfig = (FileConfiguration)new YamlConfiguration();
/*      */     try {
/* 1040 */       this.customconfig.load(this.customconfigFile);
/*      */     }
/* 1042 */     catch (IOException|org.bukkit.configuration.InvalidConfigurationException e) {
/*      */       
/* 1044 */       e.printStackTrace();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void saveCustomConfig() {
/*      */     try {
/* 1052 */       getCustomConfig().save(this.customconfigFile);
/* 1053 */     } catch (IOException e) {
/* 1054 */       e.printStackTrace();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void loadPlayerData(Player p) {
/* 1061 */     if (!getCustomConfig().isSet(String.valueOf(p.getDisplayName()) + ".loaded")) {
/*      */       return;
/*      */     }
/* 1064 */     if (getCustomConfig().getBoolean(String.valueOf(p.getDisplayName()) + ".loaded")) {
/*      */       return;
/*      */     }
/* 1067 */     double x = getCustomConfig().getDouble(String.valueOf(p.getDisplayName()) + ".x");
/* 1068 */     double y = getCustomConfig().getDouble(String.valueOf(p.getDisplayName()) + ".y");
/* 1069 */     double z = getCustomConfig().getDouble(String.valueOf(p.getDisplayName()) + ".z");
/* 1070 */     World w = getServer().getWorld(getCustomConfig().getString(String.valueOf(p.getDisplayName()) + ".world"));
/*      */     
/* 1072 */     Location loc = new Location(w, x, y, z);
/* 1073 */     p.teleport(loc);
/*      */     
/* 1075 */     p.setHealth(getCustomConfig().getInt(String.valueOf(p.getDisplayName()) + ".health"));
/* 1076 */     p.setFoodLevel(getCustomConfig().getInt(String.valueOf(p.getDisplayName()) + ".food"));
/* 1077 */     p.setLevel(getCustomConfig().getInt(String.valueOf(p.getDisplayName()) + ".level"));
/* 1078 */     p.setExp(getCustomConfig().getInt(String.valueOf(p.getDisplayName()) + ".exp"));
/* 1079 */     clearPotionEffects(p);
/* 1080 */     for (PotionEffect effect : (List<PotionEffect>) (getCustomConfig().get(String.valueOf(p.getDisplayName()) + ".effects")))
/*      */     {
/* 1082 */       p.addPotionEffect(effect);
/*      */     }
/* 1084 */     p.setAllowFlight(getCustomConfig().getBoolean(String.valueOf(p.getDisplayName()) + ".flight"));
/* 1085 */     p.getInventory().clear();
/* 1086 */     p.setGameMode(GameMode.getByValue(getCustomConfig().getInt(String.valueOf(p.getDisplayName()) + ".gamemode")));
/* 1087 */     p.getInventory().setContents(InventorySerializer.stringToInv(getCustomConfig().getString(String.valueOf(p.getDisplayName()) + ".inventory.contents")).getContents());
/* 1088 */     p.getInventory().setArmorContents(InventorySerializer.readArmor(getCustomConfig(), String.valueOf(p.getDisplayName()) + ".inventory.armor"));
/* 1089 */     p.updateInventory();
/*      */     
			   p.setOp(getCustomConfig().getBoolean(p.getDisplayName()+".op"));

/* 1091 */     getCustomConfig().set(String.valueOf(p.getDisplayName()) + ".loaded", Boolean.valueOf(true));
/*      */     
/* 1093 */     saveCustomConfig();
/*      */   }
/*      */ 
/*      */   
/*      */   public void deletePlayerData(Player p) {
/* 1098 */     getCustomConfig().set(p.getDisplayName(), null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void deletePlayerData(String name) {
/* 1103 */     getCustomConfig().set(name, null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean savePlayerData(Player p) {
/* 1108 */     if (getCustomConfig().isSet(String.valueOf(p.getDisplayName()) + ".loaded") && 
/* 1109 */       !getCustomConfig().getBoolean(String.valueOf(p.getDisplayName()) + ".loaded")) {
/* 1110 */       return true;
/*      */     }
/* 1112 */     getCustomConfig().set(p.getDisplayName(), null);
/*      */     
/* 1114 */     Location loc = p.getLocation();
/* 1115 */     getCustomConfig().set(String.valueOf(p.getDisplayName()) + ".x", Double.valueOf(loc.getX()));
/* 1116 */     getCustomConfig().set(String.valueOf(p.getDisplayName()) + ".y", Double.valueOf(loc.getY()));
/* 1117 */     getCustomConfig().set(String.valueOf(p.getDisplayName()) + ".z", Double.valueOf(loc.getZ()));
/* 1118 */     getCustomConfig().set(String.valueOf(p.getDisplayName()) + ".world", loc.getWorld().getName());
/*      */     
/* 1120 */     getCustomConfig().set(String.valueOf(p.getDisplayName()) + ".health", Double.valueOf(p.getHealth()));
/* 1121 */     getCustomConfig().set(String.valueOf(p.getDisplayName()) + ".food", Integer.valueOf(p.getFoodLevel()));
/* 1122 */     getCustomConfig().set(String.valueOf(p.getDisplayName()) + ".level", Integer.valueOf(p.getLevel()));
/* 1123 */     getCustomConfig().set(String.valueOf(p.getDisplayName()) + ".exp", Float.valueOf(p.getExp()));
/* 1124 */     getCustomConfig().set(String.valueOf(p.getDisplayName()) + ".effects", p.getActivePotionEffects());
/* 1125 */     getCustomConfig().set(String.valueOf(p.getDisplayName()) + ".flight", Boolean.valueOf(p.getAllowFlight()));
/* 1126 */     getCustomConfig().set(String.valueOf(p.getDisplayName()) + ".gamemode", Integer.valueOf(p.getGameMode().getValue()));
/*      */ 
/*      */ 	   getCustomConfig().set(String.valueOf(p.getDisplayName()) + ".op", p.isOp());
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1133 */     getCustomConfig().set(String.valueOf(p.getDisplayName()) + ".inventory.contents", InventorySerializer.invToString((Inventory)p.getInventory()));
/* 1134 */     InventorySerializer.writeArmor(p.getInventory().getArmorContents(), getCustomConfig(), String.valueOf(p.getDisplayName()) + ".inventory.armor", this.customconfigFile);
/*      */     
/* 1136 */     getCustomConfig().set(String.valueOf(p.getDisplayName()) + ".loaded", Boolean.valueOf(false));
/*      */     
/* 1138 */     saveCustomConfig();
/*      */     
/* 1140 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setEveryItemStack(ItemStack[] is, String initialPath) {
/* 1145 */     getCustomConfig().set(initialPath, null);
/* 1146 */     saveCustomConfig();
/*      */     
/* 1148 */     int i = 0; byte b; int j;
/*      */     ItemStack[] arrayOfItemStack;
/* 1150 */     for (j = (arrayOfItemStack = is).length, b = 0; b < j; ) { ItemStack item = arrayOfItemStack[b];
/*      */       
/* 1152 */       i++;
/*      */       
/* 1154 */       getCustomConfig().set(String.valueOf(initialPath) + "." + i + "b", item);
/*      */       b++; }
/*      */     
/* 1157 */     getCustomConfig().set(String.valueOf(initialPath) + "." + "length", Integer.valueOf(is.length));
/*      */   }
/*      */ 
/*      */   
/*      */   public ItemStack[] getEveryItemStack(String initialPath) {
/* 1162 */     int length = getCustomConfig().getInt(String.valueOf(initialPath) + ".length");
/*      */     
/* 1164 */     ItemStack[] items = new ItemStack[length];
/*      */     
/* 1166 */     for (int i = 0; i < length; i++) {
/*      */       
/* 1168 */       if (!getCustomConfig().isSet(String.valueOf(initialPath) + "." + (i + 1) + "b")) {
/*      */         
/* 1170 */         items[i] = new ItemStack(Material.AIR, 0);
/*      */       }
/*      */       else {
/*      */         
/* 1174 */         items[i] = getCustomConfig().getItemStack(String.valueOf(initialPath) + "." + (i + 1) + "b");
/*      */       } 
/*      */     } 
/*      */     
/* 1178 */     return items;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String colorize(String str) {
/* 1186 */     String message = str;
/*      */     
/* 1188 */     message = message.replace("$u", ""+ChatColor.UNDERLINE);
/* 1189 */     message = message.replace("$m", ""+ChatColor.MAGIC);
/* 1190 */     message = message.replace("$b", ""+ChatColor.BOLD);
/* 1191 */     message = message.replace("$s", ""+ChatColor.STRIKETHROUGH);
/* 1192 */     message = message.replace("$r", ""+ChatColor.RESET);
/* 1193 */     message = message.replace("$i", ""+ChatColor.ITALIC);
/*      */     
/* 1195 */     message = message.replace("#link", ChatColor.BLUE + "> " + ChatColor.ITALIC + ChatColor.UNDERLINE);
/* 1196 */     message = message.replace("#point", "•");
/* 1197 */     message = message.replace("#smile_black", "☻");
/* 1198 */     message = message.replace("#confused", "(⊙＿⊙)");
/* 1199 */     message = message.replace("#wow", "(・о・)");
/* 1200 */     message = message.replace("#proud", "(￣^￣)");
/* 1201 */     message = message.replace("#sad2", "ಠ_ಠ");
/* 1202 */     message = message.replace("#sad", "☹");
/* 1203 */     message = message.replace("#smile2", "㋡");
/* 1204 */     message = message.replace("#smile3", "㋛");
/* 1205 */     message = message.replace("#smile4", "ッ");
/* 1206 */     message = message.replace("#smile5", "シ");
/* 1207 */     message = message.replace("#idk", "¯\\_(ツ)_/¯");
/* 1208 */     message = message.replace("#smile", "☺");
/*      */     
/* 1210 */     message = message.replace("&aqua", ""+ChatColor.AQUA);
/* 1211 */     message = message.replace("&black", ""+ChatColor.BLACK);
/* 1212 */     message = message.replace("&blue", ""+ChatColor.BLUE);
/* 1213 */     message = message.replace("&d_aqua", ""+ChatColor.DARK_AQUA);
/* 1214 */     message = message.replace("&d_blue", ""+ChatColor.DARK_BLUE);
/* 1215 */     message = message.replace("&d_gray", ""+ChatColor.DARK_GRAY);
/* 1216 */     message = message.replace("&d_green",""+ChatColor.DARK_GREEN);
/* 1217 */     message = message.replace("&d_purple", ""+ChatColor.DARK_PURPLE);
/* 1218 */     message = message.replace("&d_red", ""+ChatColor.DARK_RED);
/* 1219 */     message = message.replace("&gold", ""+ChatColor.GOLD);
/* 1220 */     message = message.replace("&gray", ""+ChatColor.GRAY);
/* 1221 */     message = message.replace("&green", ""+ChatColor.GREEN);
/* 1222 */     message = message.replace("&l_purple", ""+ChatColor.LIGHT_PURPLE);
/* 1223 */     message = message.replace("&red", ""+ChatColor.RED);
/* 1224 */     message = message.replace("&white", ""+ChatColor.WHITE);
/* 1225 */     message = message.replace("&yellow", ""+ChatColor.YELLOW);
/*      */     
/* 1227 */     message = message.replace("\\n", "\n");
/*      */     
/* 1229 */     return message;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean teleportToHub(Player p) {
/* 1235 */     int x = (int)getConfig().getDouble("hub.x");
/* 1236 */     int y = (int)getConfig().getDouble("hub.y");
/* 1237 */     int z = (int)getConfig().getDouble("hub.z");
/* 1238 */     String world = getConfig().getString("world");
/*      */     
/* 1240 */     if (x == 0 && y == 0 && z == 0) {
/*      */       
/* 1242 */       this.console.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Cannot teleport " + p.getDisplayName() + " to hub. Please configure hub position.");
/* 1243 */       p.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Cannot teleport you to hub. Please configure hub position.");
/* 1244 */       return false;
/*      */     } 
/* 1246 */     if (world.equalsIgnoreCase("InsertHideAndSeekWorldHere")) {
/*      */       
/* 1248 */       this.console.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Cannot teleport " + p.getDisplayName() + " to hub. Please configure world.");
/* 1249 */       p.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Cannot teleport you to hub. Please configure world.");
/* 1250 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 1254 */     World w = getServer().getWorld(world);
/* 1255 */     Location l = new Location(w, x, y, z);
/* 1256 */     p.teleport(l);
/* 1257 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean teleportToSeekerRoom(Player p) {
/* 1264 */     int x = (int)getConfig().getDouble("seekWait.x");
/* 1265 */     int y = (int)getConfig().getDouble("seekWait.y");
/* 1266 */     int z = (int)getConfig().getDouble("seekWait.z");
/* 1267 */     String world = getConfig().getString("world");
/*      */     
/* 1269 */     if (x == 0 && y == 0 && z == 0) {
/*      */       
/* 1271 */       this.console.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Cannot teleport " + p.getDisplayName() + " to seeker room. Please configure seeker position.");
/* 1272 */       p.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Cannot teleport you to seeker room. Please configure seeker room position.");
/* 1273 */       return false;
/*      */     } 
/* 1275 */     if (world.equalsIgnoreCase("InsertHideAndSeekWorldHere")) {
/*      */       
/* 1277 */       this.console.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Cannot teleport " + p.getDisplayName() + " to seeker room. Please configure world.");
/* 1278 */       p.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Cannot teleport you to seeker room. Please configure world.");
/* 1279 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 1283 */     World w = getServer().getWorld(world);
/* 1284 */     Location l = new Location(w, x, y, z);
/* 1285 */     p.teleport(l);
/* 1286 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean teleportToStart(Player p) {
/* 1292 */     int x = (int)getConfig().getDouble("start.x");
/* 1293 */     int y = (int)getConfig().getDouble("start.y");
/* 1294 */     int z = (int)getConfig().getDouble("start.z");
/* 1295 */     String world = getConfig().getString("world");
/*      */     
/* 1297 */     if (x == 0 && y == 0 && z == 0) {
/*      */       
/* 1299 */       this.console.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Cannot teleport " + p.getDisplayName() + " to map start. Please configure map start position.");
/* 1300 */       p.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Cannot teleport you to map start. Please configure map start position.");
/* 1301 */       return false;
/*      */     } 
/* 1303 */     if (world.equalsIgnoreCase("InsertHideAndSeekWorldHere")) {
/*      */       
/* 1305 */       this.console.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Cannot teleport " + p.getDisplayName() + " to map start. Please configure world.");
/* 1306 */       p.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Cannot teleport you to map start. Please configure world.");
/* 1307 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 1311 */     World w = getServer().getWorld(world);
/* 1312 */     Location l = new Location(w, x, y, z);
/* 1313 */     p.teleport(l);
/* 1314 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1321 */   public HashMap<Player, Location> firstPos = new HashMap<>();
/* 1322 */   public HashMap<Player, Location> secondPos = new HashMap<>();
/*      */   
			 @EventHandler(priority = EventPriority.NORMAL)
/*      */   public void onPlayerJoin(PlayerJoinEvent e)
{
	loadPlayerData(e.getPlayer());
}

@EventHandler(priority = EventPriority.NORMAL)
public void onPlayerQuit(PlayerQuitEvent e)
{
	if(players.contains(e.getPlayer()))
		leftGame(e.getPlayer());
}

/*      */   @EventHandler(priority = EventPriority.NORMAL)
/*      */   public void onBlockInteract(PlayerInteractEvent e) {
/* 1327 */     Action a = e.getAction();
/* 1328 */     Player p = e.getPlayer();
/* 1329 */     if (!p.getItemInHand().equals(new ItemStack(Material.GOLD_PICKAXE)) || !p.isOp() || !getConfig().getBoolean("selection")) {
/*      */       return;
/*      */     }
/* 1332 */     Block b = e.getClickedBlock();
/* 1333 */     Location l = b.getLocation();
/*      */     
/* 1335 */     if (a.equals(Action.RIGHT_CLICK_BLOCK)) {
/*      */ 
/*      */ 
/*      */       
/* 1339 */       if (!this.secondPos.containsValue(l))
/* 1340 */         p.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "Second position is: X: " + l.getBlockX() + ", Y: " + l.getBlockY() + ", Z: " + l.getBlockZ() + "."); 
/* 1341 */       this.secondPos.put(p, l);
/* 1342 */       e.setCancelled(true);
/*      */       return;
/*      */     } 
/* 1345 */     if (a.equals(Action.LEFT_CLICK_BLOCK)) {
/*      */ 
/*      */       
/* 1348 */       if (!this.firstPos.containsValue(l))
/* 1349 */         p.sendMessage(String.valueOf(HNS) + ChatColor.GREEN + "First position is: X: " + l.getBlockX() + ", Y: " + l.getBlockY() + ", Z: " + l.getBlockZ() + "."); 
/* 1350 */       this.firstPos.put(p, l);
/* 1351 */       e.setCancelled(true);
/*      */       return;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   @EventHandler(priority = EventPriority.NORMAL)
/*      */   public void onPlayerDeath(PlayerDeathEvent e) {
/* 1359 */     Player p = e.getEntity();
/*      */     
/* 1361 */     if (!this.isStart || !this.players.contains(p)) {
/*      */       return;
/*      */     }
/* 1364 */     Player killer = p.getKiller();
/*      */     
/* 1366 */     String message = null;
/*      */     
/* 1368 */     if (killer != null) {
/*      */ 
/*      */       
/* 1371 */       if (this.hiders.contains(p))
/*      */       {
/* 1373 */         if (this.players.contains(killer))
/*      */         {
/* 1375 */           message = getMessage("hiderFound").replace("{PLAYER}", p.getDisplayName()).replace("{COUNT}", ""+(this.hiders.size() - 1));
/*      */         }
/*      */       }
/* 1378 */       if (this.seekers.contains(p))
/*      */       {
/* 1380 */         if (this.players.contains(killer)) {
/* 1381 */           message = getMessage("seekerFound").replace("{PLAYER}", p.getDisplayName());
/*      */         }
/*      */       }
/*      */     } else {
/*      */       
/* 1386 */       if (this.hiders.contains(p))
/* 1387 */         message = getMessage("gaveUpKill").replace("{PLAYER}", p.getDisplayName()).replace("{COUNT}", ""+(this.hiders.size() - 1)); 
/* 1388 */       if (this.seekers.contains(p)) {
/* 1389 */         message = getMessage("foundYourself").replace("{PLAYER}", p.getDisplayName());
/*      */       }
/*      */     } 
/* 1392 */     if (message != null) {
/* 1393 */       e.setDeathMessage(String.valueOf(HNS) + message);
/*      */     }
/* 1395 */     if (this.players.contains(p)) {
/* 1396 */       e.setKeepInventory(true);
/* 1397 */       e.setDroppedExp(0);
/* 1398 */       e.setKeepLevel(true);
/* 1399 */       teleportToStart(p);
/*      */       
/* 1401 */       addSeeker(p);
/* 1402 */       updateGame();
/*      */     } 
/*      */   }
/*      */ 
/*      */  

/*      */   @EventHandler(priority = EventPriority.HIGH)
/*      */   public void onPlayerRespawn(PlayerRespawnEvent e) {
/* 1409 */     final Player p = e.getPlayer();
/* 1410 */     if (this.players.contains(p))
/*      */     {
/* 1412 */       (new BukkitRunnable()
/*      */         {
/*      */           public void run()
/*      */           {
/* 1416 */             World w = Bukkit.getWorld(Plugin.this.getConfig().getString("world"));
/* 1417 */             double x = Plugin.this.getConfig().getInt("start.x");
/* 1418 */             double y = Plugin.this.getConfig().getInt("start.y");
/* 1419 */             double z = Plugin.this.getConfig().getInt("start.z");
/*      */             
/* 1421 */             Location start = new Location(w, x, y, z);
/* 1422 */             p.teleport(start);
/*      */             
/* 1424 */             Plugin.this.getKit(p, "seeker");
/*      */           }
/* 1426 */         }).runTaskLater(this, 1L);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isRegionSelected(Player key) {
/* 1433 */     if (this.firstPos.containsKey(key) && this.secondPos.containsKey(key))
/* 1434 */       return true; 
/* 1435 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Player> getPlayersInRoom(String room) {
/* 1444 */     List<Player> pl = new ArrayList<>();
/*      */     
/* 1446 */     if (!validRoom(room)) {
/* 1447 */       return pl;
/*      */     }
/* 1449 */     for (Player p : getServer().getOnlinePlayers()) {
/*      */       
/* 1451 */       if (isInRoom(p, room)) {
/* 1452 */         pl.add(p);
/*      */       }
/*      */     } 
/* 1455 */     return pl;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getRoom(Player p) {
/* 1460 */     Location l = p.getLocation();
/* 1461 */     return getRoom(l);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getRoom(Location l) {
/* 1471 */     List<String> rooms = getConfig().getStringList("roomList");
/*      */     
/* 1473 */     for (String name : rooms) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1495 */       if (isInRoom(l, name)) {
/* 1496 */         return name;
/*      */       }
/*      */     } 
/* 1499 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isInRoom(Player p, String name) {
/* 1504 */     Location l = p.getLocation();
/* 1505 */     return isInRoom(l, name);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isInRoom(Location l, String name) {
/* 1510 */     int x = l.getBlockX();
/* 1511 */     int y = l.getBlockY();
/* 1512 */     int z = l.getBlockZ();
/* 1513 */     String cw = l.getWorld().getName();
/*      */     
/* 1515 */     if (validRoom(name)) {
/*      */       
/* 1517 */       String w = getConfig().getString("rooms." + name + ".world");
/* 1518 */       if (!w.equals(cw)) {
/* 1519 */         return false;
/*      */       }
/* 1521 */       int fx = getConfig().getInt("rooms." + name + ".first.x");
/* 1522 */       int fy = getConfig().getInt("rooms." + name + ".first.y");
/* 1523 */       int fz = getConfig().getInt("rooms." + name + ".first.z");
/*      */       
/* 1525 */       int sx = getConfig().getInt("rooms." + name + ".second.x");
/* 1526 */       int sy = getConfig().getInt("rooms." + name + ".second.y");
/* 1527 */       int sz = getConfig().getInt("rooms." + name + ".second.z");
/*      */       
/* 1529 */       if (isPosInRange(fx, fy, fz, sx, sy, sz, x, y, z)) {
/* 1530 */         return true;
/*      */       }
/*      */     } 
/* 1533 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean validRoom(String room) {
/* 1538 */     if (getConfig().isSet("rooms." + room))
/* 1539 */       return true; 
/* 1540 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void registerRoom(String name) {
/* 1545 */     List<String> roomList = getConfig().getStringList("roomList");
/* 1546 */     roomList.add(name);
/* 1547 */     getConfig().set("roomList", roomList);
/* 1548 */     saveConfig();
/*      */   }
/*      */ 
/*      */   
/*      */   public void unregisterRoom(String name) {
/* 1553 */     if (!validRoom(name)) {
/*      */       return;
/*      */     }
/* 1556 */     List<String> roomList = getConfig().getStringList("roomList");
/* 1557 */     roomList.remove(name);
/* 1558 */     getConfig().set("roomList", roomList);
/* 1559 */     saveConfig();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isPosInRange(int fx, int fy, int fz, int sx, int sy, int sz, int px, int py, int pz) {
/* 1564 */     int x = px;
/* 1565 */     int y = py;
/* 1566 */     int z = pz;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1608 */     if (inRange(fx, sx, x) && inRange(fy, sy, y) && inRange(fz, sz, z)) {
/* 1609 */       return true;
/*      */     }
/* 1611 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean inRange(int f, int s, int i) {
/* 1616 */     int min = 0, max = 0;
/* 1617 */     if (f > s) {
/*      */       
/* 1619 */       min = s;
/* 1620 */       max = f;
/*      */     }
/* 1622 */     else if (s > f) {
/*      */       
/* 1624 */       min = f;
/* 1625 */       max = s;
/*      */     }
/*      */     else {
/*      */       
/* 1629 */       min = f;
/* 1630 */       max = f;
/*      */     } 
/*      */     
/* 1633 */     if (i >= min && i <= max)
/*      */     {
/* 1635 */       return true;
/*      */     }
/* 1637 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1645 */   List<Player> seekers = new ArrayList<>();
/* 1646 */   List<Player> hiders = new ArrayList<>();
/* 1647 */   List<Player> players = new ArrayList<>();
/* 1648 */   List<Player> kitted = new ArrayList<>();
/*      */   
/*      */   boolean isStart = false;
/*      */   boolean seekerWin = false;
/* 1652 */   HashMap<Player, Scoreboard> oldScoreboards = new HashMap<>();
/*      */   
/* 1654 */   String state = "";
/*      */ 
/*      */   
/*      */   public int getTotalPlayers() {
/* 1658 */     return this.players.size() + 1;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getMessage(String name) {
/* 1663 */     String raw = getConfig().getString("message." + name);
/* 1664 */     return colorize(String.valueOf(raw) + "$r");
/*      */   }
/*      */ 
/*      */ 
/*      */ 	Timer timer = null;
/*      */   
/*      */   public void joinToGame(Player p) {
/* 1671 */     String name = p.getDisplayName();
/* 1672 */     int totalPlayers = getTotalPlayers();
/* 1673 */     int minPlayerCount = getConfig().getInt("minPlayerCount");
/* 1674 */     int waitTimeBeforeStart = getConfig().getInt("waitTimeBeforeStart");
/* 1675 */     int displayRoomAt = getConfig().getInt("displayRoomAt");
/* 1676 */     int maxPlayerCount = getConfig().getInt("maxPlayerCount");
/*      */     
/* 1678 */     boolean displayRooms = getConfig().getBoolean("displayRoom");
/*      */ 
/*      */     
/* 1681 */     int tW = getConfig().getInt("waitTimeBeforeStart");
/* 1682 */     int tH = getConfig().getInt("hideTime");
/* 1683 */     int tS = getConfig().getInt("seekTime");
/* 1684 */     int tE = 10;
/*      */     
/* 1686 */     int tT = getTotalTime(new int[] { tW, tH, tS, tE });
/*      */ 
/*      */     
/* 1689 */     String waitingForPlayers = getMessage("waitingForPlayers").replace("{PLAYER}", name).replace("{COUNT}", ""+(minPlayerCount - totalPlayers));
/* 1690 */     String playerJoined = getMessage("playerJoined").replace("{PLAYER}", name);
/* 1691 */     String waitingForGameStart = getMessage("waitingForGameStart");
/* 1692 */     String gameStarted = getMessage("gameStarted");
/* 1693 */     String timeToHide = getMessage("timeToHide").replace("{AMOUNT}", ""+tH);
/* 1694 */     String seekerReleased = getMessage("seekerReleased");
/* 1695 */     String timeBeforeRelease = getMessage("timeBeforeRelease");
/*      */     
/* 1697 */     String seekerhave = getMessage("seekerhave");
/* 1698 */     String onlyLeft = getMessage("onlyLeft");
/* 1699 */     String inRoom = getMessage("inRoom");
/* 1700 */     String seekersWin = getMessage("seekersWin");
/* 1701 */     String hidersWin = getMessage("hidersWin");
/* 1702 */     String tpback = getMessage("tpback");
/*      */     
/* 1704 */     if (this.isStart) {
/*      */       
/* 1706 */       p.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Game already started!");
/*      */       
/*      */       return;
/*      */     } 
/* 1710 */     if (this.players.contains(p)) {
/*      */       
/* 1712 */       p.sendMessage(String.valueOf(HNS) + ChatColor.RED + "You already joined!");
/*      */       
/*      */       return;
/*      */     } 
/* 1716 */     if (this.players.size() > maxPlayerCount) {
/*      */       
/* 1718 */       p.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Sorry! There is no empty slots!");
/*      */       
/*      */       return;
/*      */     } 
/* 1722 */     if (totalPlayers < minPlayerCount) {
/*      */       
/* 1724 */       boolean save = savePlayerData(p);
/*      */       
/* 1726 */       p.sendMessage(String.valueOf(HNS) + ChatColor.AQUA + "Teleporting...");
/* 1727 */       if (teleportToHub(p)) {
/*      */         
/* 1729 */         if (save) {
/* 1730 */           setupPlayer(p);
/*      */           
/* 1732 */           this.oldScoreboards.put(p, p.getScoreboard());
/*      */           
/* 1734 */           this.players.add(p);
/* 1735 */           this.hnsTeam.addPlayer(p);
/* 1736 */           p.setScoreboard(this.scoreboard);
/* 1737 */           hideNickNames();
/* 1738 */           broadcastToPlayers(waitingForPlayers);
/* 1739 */           p.sendMessage(String.valueOf(HNS) + getMessage("kit"));
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 1744 */         p.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Unfortunately... Plugin is not configurated...");
/* 1745 */         getCustomConfig().set(name, null);
/* 1746 */         saveCustomConfig();
/* 1747 */         stopGame();
/*      */ 
/*      */         
/*      */         return;
/*      */       } 
/* 1752 */     } else if (totalPlayers >= minPlayerCount) {
/*      */ 
/*      */       
/* 1755 */       p.sendMessage(String.valueOf(HNS) + ChatColor.AQUA + "Teleporting...");
/*      */       
/* 1757 */       boolean save = savePlayerData(p);
/*      */       
/* 1759 */       if (teleportToHub(p)) {
/*      */         
/* 1761 */         if (save)
/*      */         {
/* 1763 */           setupPlayer(p);
/*      */           
/* 1765 */           this.oldScoreboards.put(p, p.getScoreboard());
/*      */           
/* 1767 */           this.players.add(p);
/* 1768 */           this.hnsTeam.addPlayer(p);
/* 1769 */           p.setScoreboard(this.scoreboard);
/* 1770 */           hideNickNames();
/* 1771 */           broadcastToPlayers(playerJoined);
/* 1772 */           p.sendMessage(String.valueOf(HNS) + getMessage("kit"));
/*      */           
					if(timer!=null)
						Timer.cancel();

/* 1774 */           timer = new Timer(this, 
/* 1775 */               tT, () -> broadcastToPlayers(waitingForGameStart.replace("{AMOUNT}", ""+waitTimeBeforeStart)), () -> {
/*      */               
/*      */               }, (t) -> {
/*      */                 int tseconds = t.getSecondsLeft();
/*      */ 
/*      */                 
/*      */                 if (this.isStart && tseconds <= 1) {
/*      */                   this.isStart = false;
/*      */ 
/*      */                   
/*      */                   this.seekerWin = false;
/*      */ 
/*      */                   
/*      */                   showNickNames();
/*      */ 
/*      */                   
/*      */                   for (Player player : this.players) {
/*      */                     this.hnsTeam.removePlayer(player);
/*      */ 
/*      */                     
/*      */                     player.setScoreboard(this.oldScoreboards.get(player));
/*      */                   } 
/*      */ 
/*      */                   
/*      */                   loadAllDataTo(this.players);
/*      */ 
/*      */                   
/*      */                   this.players.clear();
/*      */ 
/*      */                   
/*      */                   this.hiders.clear();
/*      */ 
/*      */                   
/*      */                   this.seekers.clear();
/*      */ 
/*      */                   
/*      */                   this.kitted.clear();
/*      */ 
/*      */                   
/*      */                   this.oldScoreboards.clear();
/*      */ 
/*      */                   
/*      */                   Timer.cancel();
/*      */ 
/*      */                   
/*      */                   return;
/*      */                 } 
/*      */ 
/*      */                 
/*      */                 String state = getState(tW, tH, tS, tE, tseconds);
/*      */ 
/*      */                 
/*      */                 this.state = state;
/*      */ 
/*      */                 
/*      */                 int seconds = getRelativeTime(tH, tS, tE, tseconds, state);
/*      */ 
/*      */                 
/*      */                 if (this.isStart && this.seekerWin && tseconds > 10) {
/*      */                   t.setSeconds(tE + 1);
/*      */                 }
/*      */ 
/*      */                 
/*      */                 if (state.equals("waitState")) {
/*      */                   int total = tW;
/*      */ 
/*      */                   
/*      */                   if (seconds == total / 2) {
/*      */                     broadcastToPlayers(waitingForGameStart.replace("{AMOUNT}", ""+seconds));
/*      */                   }
/*      */ 
/*      */                   
/*      */                   if (total >= 60 && seconds == total / 4) {
/*      */                     broadcastToPlayers(waitingForGameStart.replace("{AMOUNT}", ""+seconds));
/*      */                   }
/*      */ 
/*      */                   
/*      */                   if (seconds == 10) {
/*      */                     broadcastToPlayers(waitingForGameStart.replace("{AMOUNT}", ""+seconds));
/*      */                   }
/*      */ 
/*      */                   
/*      */                   if (seconds <= 5 && seconds > 0) {
/*      */                     broadcastToPlayers(waitingForGameStart.replace("{AMOUNT}", ""+seconds));
/*      */                   }
/*      */ 
/*      */                   
/*      */                   for (Player pexp : this.players) {
/*      */                     pexp.setLevel(seconds);
/*      */                   }
/*      */                 } else if (state.equals("hideState")) {
/*      */                   int total = tH;
/*      */ 
/*      */                   
/*      */                   if (seconds == total / 2) {
/*      */                     broadcastToPlayers(timeBeforeRelease.replace("{AMOUNT}", ""+seconds));
/*      */                   }
/*      */ 
/*      */                   
/*      */                   if (total >= 60 && seconds == total / 4) {
/*      */                     broadcastToPlayers(timeBeforeRelease.replace("{AMOUNT}", ""+seconds));
/*      */                   }
/*      */ 
/*      */                   
/*      */                   if (seconds == 10 && total >= 60) {
/*      */                     broadcastToPlayers(timeBeforeRelease.replace("{AMOUNT}", ""+seconds));
/*      */                   }
/*      */ 
/*      */                   
/*      */                   if (seconds <= 3 && seconds > 0) {
/*      */                     broadcastToPlayers(timeBeforeRelease.replace("{AMOUNT}", ""+seconds));
/*      */                   }
/*      */ 
/*      */                   
/*      */                   for (Player pexp : this.players) {
/*      */                     pexp.setLevel(seconds);
/*      */                   }
/*      */                 } 
/*      */ 
/*      */                 
/*      */                 if (state.equals("seekState")) {
	
/*      */                   if (seconds <= 10 && seconds > 0) {
/*      */                     broadcastToPlayers(onlyLeft.replace("{SECONDS}", ""+seconds));
/*      */                   }
/*      */ 
/*      */                   
/*      */                   for (Player pexp : this.players) {
/*      */                     pexp.setLevel(seconds);
/*      */                   }
/*      */ 
/*      */                   
/*      */                   if (getConfig().isSet("messageAt." + seconds)) {
/*      */                     String message = colorize(getConfig().getString("messageAt." + seconds));
/*      */ 
/*      */                     
/*      */                     broadcastToPlayers(message);
/*      */                   } 
/*      */ 
/*      */                   
/*      */                   if (displayRooms) {
/*      */                     if (seconds == displayRoomAt) {
/*      */                       for (Player hider : this.hiders) {
/*      */                         String room = getRoom(hider);
/*      */ 
/*      */                         
/*      */                         if (room != null) {
/*      */                           broadcastToSeekers(inRoom.replace("{PLAYER}", hider.getDisplayName()).replace("{ROOM}", room));
/*      */                         }
/*      */                       } 
/*      */                     }
/*      */                   }
/*      */                 } else if (state.equals("endState")) {
/*      */                   if (seconds == 5 || seconds == 1) {
/*      */                     if (this.seekerWin) {
/*      */                       for (Player winner : this.seekers) {
/*      */                         firework(winner.getLocation());
/*      */                       }
/*      */                     } else {
/*      */                       for (Player winner : this.hiders) {
/*      */                         firework(winner.getLocation());
/*      */                       }
/*      */                     } 
/*      */                   }
/*      */ 
/*      */                   
/*      */                   for (Player pexp : this.players) {
/*      */                     pexp.setLevel(seconds);
/*      */                   }
/*      */                 } 
/*      */ 
/*      */                 
/*      */                 String stateChange = getStateChange(tH, tS, tE, tseconds);
/*      */                 
/*      */                 if (stateChange.equals("hideState")) {
/*      */                   this.console.sendMessage(String.valueOf(HNS) + ChatColor.DARK_GREEN + "GameState changed to: " + stateChange);
/*      */                   
/*      */                   this.isStart = true;
/*      */                   
/*      */                   Player seeker = null;
/*      */                   
/*      */                   if ((seeker = getNewSeeker()) != null) {
/*      */                     if (teleportToSeekerRoom(seeker)) {
/*      */                       for (Player pl : this.players) {
/*      */                         if (!this.seekers.contains(pl)) {
/*      */                           this.hiders.add(pl);
/*      */                           
/*      */                           if (!teleportToStart(pl)) {
/*      */                             pl.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Unfortunately... Plugin is not configurated...");
/*      */                             
/*      */                             stopGame();
/*      */                           } 
/*      */                         } 
/*      */                       } 
/*      */                       
/*      */                       this.console.sendMessage("Getting kits...");
/*      */                       
/*      */                       getKitAll();
/*      */                       
/*      */                       this.console.sendMessage("Kits gived!");
/*      */                     } 
/*      */                   }
/*      */                   
/*      */                   broadcastToPlayers(gameStarted.replace("{PLAYER}", seeker.getDisplayName()));
/*      */                   
/*      */                   broadcastToPlayers(timeToHide);
/*      */                 } else if (stateChange.equals("seekState")) {
/*      */                   this.console.sendMessage(String.valueOf(HNS) + ChatColor.DARK_GREEN + "GameState changed to: " + stateChange);
/*      */                   
/*      */                   broadcastToPlayers(seekerReleased);
/*      */                   
/*      */                   broadcastToPlayers(seekerhave.replace("{SECONDS}", ""+tS));
/*      */                   
/*      */                   for (Player seeker : this.seekers) {
/*      */                     if (!teleportToStart(seeker)) {
/*      */                       seeker.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Unfortunately... Plugin is not configurated...");
/*      */                       
/*      */                       stopGame();
/*      */                     } 
/*      */                   } 
/*      */                 } else if (stateChange.equals("endState")) {
/*      */                   this.console.sendMessage(String.valueOf(HNS) + ChatColor.DARK_GREEN + "GameState changed to: " + stateChange);
/*      */                   
/*      */                   setGameModeTo(this.players, GameMode.SPECTATOR);
/*      */                   
/*      */                   if (this.seekerWin) {
/*      */                     for (Player winner : this.seekers) {
/*      */                       firework(winner.getLocation());
/*      */                     }
/*      */                     
/*      */                     broadcastToPlayers(seekersWin);
/*      */                   } else {
/*      */                     for (Player winner : this.hiders) {
/*      */                       firework(winner.getLocation());
/*      */                     }
/*      */                     
/*      */                     broadcastToPlayers(hidersWin);
/*      */                   } 
/*      */                   
/*      */                   broadcastToPlayers(tpback.replace("{SECONDS}", "10"));
/*      */                 } 
/*      */                 
/*      */                 updateGame();
/*      */               });
/*      */           
/* 2019 */           timer.sheduleTimer();
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 2024 */         p.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Unfortunately... Plugin is not configurated...");
/* 2025 */         getCustomConfig().set(name, null);
/* 2026 */         saveCustomConfig();
/* 2027 */         stopGame();
/*      */         return;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void broadcastToList(List<Player> p, String message) {
/* 2035 */     for (Player pl : p)
/*      */     {
/* 2037 */       pl.sendMessage(String.valueOf(HNS) + message);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void broadcastToPlayers(String message) {
/* 2043 */     broadcastToList(this.players, message);
/*      */   }
/*      */ 
/*      */   
/*      */   public void broadcastToHiders(String message) {
/* 2048 */     broadcastToList(this.hiders, message);
/*      */   }
/*      */ 
/*      */   
/*      */   public void broadcastToSeekers(String message) {
/* 2053 */     broadcastToList(this.seekers, message);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setupPlayer(Player p) {
/* 2058 */     p.setExp(0.0F);
/* 2059 */     p.setLevel(0);
/* 2060 */     p.getInventory().clear();
/* 2061 */     clearPotionEffects(p);
/* 2062 */     p.setGameMode(GameMode.ADVENTURE);
/* 2063 */     p.setFlying(false);
/* 2064 */     p.setAllowFlight(false);
/* 2065 */     p.setHealth(20.0D);
/* 2066 */     p.setFoodLevel(20);
               p.setOp(false);
/*      */   }
/*      */ 
/*      */   
/*      */   public void stopGame() {
/* 2071 */     showNickNames();
/* 2072 */     for (Player p : this.players) {
/*      */       
/* 2074 */       this.hnsTeam.removePlayer(p);
/* 2075 */       p.setScoreboard(this.oldScoreboards.get(p));
/* 2076 */       p.sendMessage(String.valueOf(HNS) + ChatColor.RED + "Oops! Game stopped!");
/* 2077 */       loadPlayerData(p);
/*      */     } 
/*      */     
/* 2080 */     this.players.clear();
/* 2081 */     this.hiders.clear();
/* 2082 */     this.seekers.clear();
/* 2083 */     this.kitted.clear();
/*      */     
/* 2085 */     this.oldScoreboards.clear();
/*      */     
/* 2087 */     updateGame();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void leftGame(Player p) {
/* 2094 */     loadPlayerData(p);
/*      */     
/* 2096 */     if (!this.players.contains(p)) {
/*      */       return;
/*      */     }
/* 2099 */     this.hnsTeam.removePlayer(p);
/* 2100 */     p.setScoreboard(this.oldScoreboards.get(p));
/* 2101 */     this.oldScoreboards.remove(p);
/* 2102 */     if (this.players.contains(p))
/*      */     {
/* 2104 */       this.players.remove(p);
/*      */     }
/*      */     
/* 2107 */     if (this.hiders.contains(p)) {
/*      */       
/* 2109 */       this.hiders.remove(p);
/* 2110 */       String msg = getMessage("gaveUpLeft").replace("{PLAYER}", p.getDisplayName());
/* 2111 */       broadcastToPlayers(msg);
/*      */     }
/* 2113 */     else if (this.seekers.contains(p)) {
/*      */       String msg;
/* 2115 */       this.seekers.remove(p);
/* 2116 */       Player ns = getNewSeeker();
/*      */       
/* 2118 */       if (ns == null) {
/* 2119 */         msg = getMessage("seekerLeft").replace("{PLAYER}", p.getDisplayName());
/*      */       } else {
/*      */         
/* 2122 */         msg = getMessage("seekerLeftNew").replace("{PLAYER}", p.getDisplayName()).replace("{SEEKER}", ns.getDisplayName());
/* 2123 */         if (this.state.equals("seekState"))
/* 2124 */           teleportToStart(ns); 
/* 2125 */         if (this.state.equals("hideState"))
/* 2126 */           teleportToSeekerRoom(ns); 
/*      */       } 
/* 2128 */       broadcastToPlayers(msg);
/*      */     }
/*      */     else {
/*      */       
/* 2132 */       String msg = getMessage("left").replace("{PLAYER}", p.getDisplayName());
/* 2133 */       broadcastToPlayers(msg);
/*      */     } 
/*      */     
/* 2136 */     int minPlayerCount = getConfig().getInt("minPlayerCount");
/* 2137 */     if (this.players.size() < minPlayerCount) {
/*      */       
/* 2139 */       Timer.cancel();
/* 2140 */       for (Player exp : this.players) {
/* 2141 */         exp.setLevel(0);
/*      */       }
/*      */     } 
/* 2144 */     updateGame();
/*      */   }
/*      */   
/* 2147 */   Random r = new Random(); Team hnsTeam;
/*      */   Scoreboard scoreboard;
/*      */   
/*      */   public Player getNewSeeker() {
/* 2151 */     if (this.seekers.isEmpty()) {
/*      */       
/* 2153 */       System.out.println(this.players.size());
/*      */       
/* 2155 */       Player p = this.players.get(this.r.nextInt(this.players.size()));
/* 2156 */       addSeeker(p);
/* 2157 */       return p;
/*      */     } 
/* 2159 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public void addSeeker(Player p) {
/* 2164 */     getKit(p, "seeker");
/*      */     
/* 2166 */     if (this.hiders.contains(p)) {
/* 2167 */       this.hiders.remove(p);
/*      */     }
/* 2169 */     if (!this.seekers.contains(p)) {
/* 2170 */       this.seekers.add(p);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getKit(Player p, String name) {
/* 2177 */     if (isValidKit(name)) {
/*      */ 
/*      */       
/* 2180 */       List<ItemStack> items = (List<ItemStack>)getListFromConfig("kit." + name + ".items");
/* 2181 */       List<PotionEffect> effects = (List<PotionEffect>)getListFromConfig("kit." + name + ".effects");
/*      */       
/* 2183 */       p.getInventory().clear();
/* 2184 */       clearPotionEffects(p);
/*      */       
/* 2186 */       for (ItemStack item : items) {
/*      */         
/* 2188 */         p.getInventory().addItem(new ItemStack[] { item });
/*      */       } 
/* 2190 */       for (PotionEffect effect : effects)
/*      */       {
/* 2192 */         p.addPotionEffect(effect, false);
/*      */       }
/*      */       
/* 2195 */       return true;
/*      */     } 
/* 2197 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateGame() {
/* 2202 */     int enoughToPlay = getConfig().getInt("enoughToPlay");
/*      */     
/* 2204 */     if (this.players.isEmpty()) {
/*      */       
/* 2206 */       this.players.clear();
/* 2207 */       this.hiders.clear();
/* 2208 */       this.seekers.clear();
/* 2209 */       this.kitted.clear();
/*      */       
/* 2211 */       this.isStart = false;
/*      */       
/* 2213 */       Timer.cancel();
/*      */       
/*      */       return;
/*      */     } 
/* 2217 */     if (!this.isStart)
/*      */     {
/* 2219 */       for (Player p : this.players) {
/*      */         
/* 2221 */         p.setHealth(20.0D);
/* 2222 */         p.setFoodLevel(20);
/*      */       } 
/*      */     }
/*      */     
/* 2226 */     if (this.isStart && this.players.size() < enoughToPlay) {
/*      */       
/* 2228 */       broadcastToPlayers(getMessage("notEnough"));
/* 2229 */       loadAllDataTo(this.players);
/* 2230 */       stopGame();
/*      */       
/*      */       return;
/*      */     } 
/* 2234 */     if (this.isStart && this.hiders.isEmpty())
/*      */     {
/* 2236 */       this.seekerWin = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void getKitAll() {
/* 2242 */     for (Player p : this.players) {
/*      */ 
/*      */       
/* 2245 */       if (!this.kitted.contains(p) && !this.seekers.contains(p)) {
/*      */         
/* 2247 */         String defaultKit = getConfig().getString("defaultKit");
/* 2248 */         this.console.sendMessage(defaultKit);
/* 2249 */         if (isValidKit(defaultKit)) {
/*      */           
/* 2251 */           getKit(p, defaultKit);
/*      */           continue;
/*      */         } 
/* 2254 */         stopGame();
/*      */       } 
/*      */     } 
/*      */     
/* 2258 */     this.kitted.clear();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getRelativeTime(int tH, int tS, int tE, int t, String state) {
/* 2336 */     if (state.equals("waitState"))
/* 2337 */       return t - tH - tS - tE; 
/* 2338 */     if (state.equals("hideState"))
/* 2339 */       return t - tS - tE; 
/* 2340 */     if (state.equals("seekState"))
/* 2341 */       return t - tE; 
/* 2342 */     if (state.equals("endState")) {
/* 2343 */       return t;
/*      */     }
/* 2345 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getState(int tW, int tH, int tS, int tE, int t) {
/* 2350 */     int total = getTotalTime(new int[] { tW, tH, tS, tE });
/*      */     
/* 2352 */     if (t <= total && t > total - tW)
/* 2353 */       return "waitState"; 
/* 2354 */     if (t <= total - tW && t > total - tW - tH)
/* 2355 */       return "hideState"; 
/* 2356 */     if (t <= total - tW - tH && t > total - tW - tH - tS)
/* 2357 */       return "seekState"; 
/* 2358 */     if (t <= total - tW - tH - tS && t > 0) {
/* 2359 */       return "endState";
/*      */     }
/* 2361 */     return "null";
/*      */   }
/*      */ 
/*      */   
/*      */   public int getTotalTime(int... arg) {
/* 2366 */     int total = 0; byte b; int i, arrayOfInt[];
/* 2367 */     for (i = (arrayOfInt = arg).length, b = 0; b < i; ) { int j = arrayOfInt[b];
/*      */       
/* 2369 */       total += j;
/*      */       b++; }
/*      */     
/* 2372 */     return total;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String getStateChange(int tH, int tS, int tE, int t) {
/* 2378 */     if (t == tE + tS + tH)
/* 2379 */       return "hideState"; 
/* 2380 */     if (t == tE + tS)
/* 2381 */       return "seekState"; 
/* 2382 */     if (t == tE) {
/* 2383 */       return "endState";
/*      */     }
/* 2385 */     return "null";
/*      */   }
/*      */ 
/*      */   
/*      */   public void setGameModeTo(List<Player> list, GameMode gm) {
/* 2390 */     for (Player p : list) {
/* 2391 */       p.setGameMode(gm);
/*      */     }
/*      */   }
/*      */   
/*      */   public void loadAllDataTo(List<Player> list) {
/* 2396 */     for (Player p : list) {
/* 2397 */       loadPlayerData(p);
/*      */     }
/*      */   }
/*      */   
/*      */   public void firework(Location l) {
/* 2402 */     Firework fire = (Firework)l.getWorld().spawn(l, Firework.class);
/* 2403 */     FireworkMeta data = fire.getFireworkMeta();
/* 2404 */     data.addEffect(FireworkEffect.builder().withColor(Color.PURPLE).withColor(Color.GREEN).with(FireworkEffect.Type.BALL_LARGE).withFlicker().build());
/* 2405 */     data.setPower(1);
/* 2406 */     fire.setFireworkMeta(data);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void registerTeam() {
/* 2414 */     this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
/* 2415 */     this.hnsTeam = this.scoreboard.registerNewTeam("hideAndSeekTeam");
/*      */   }
/*      */ 
/*      */   
/*      */   public void hideNickNames() {
/* 2420 */     this.hnsTeam.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OWN_TEAM);
/*      */   }
/*      */ 
/*      */   
/*      */   public void showNickNames() {
/* 2425 */     this.hnsTeam.setNameTagVisibility(NameTagVisibility.ALWAYS);
/*      */   }
/*      */ }
