/*     */ package com.jnngl.plugin.data;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.bukkit.enchantments.Enchantment;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ 
/*     */ 
/*     */ public class InventorySerializer
/*     */ {
/*  18 */   static String sep = "•";
/*  19 */   static String blockSep = "†";
/*     */   
/*     */   public static String invToString(Inventory inventory) {
/*  22 */     String serInv = String.valueOf(Math.round((inventory.getSize() / 9))) + blockSep;
/*  23 */     serInv = String.valueOf(serInv) + inventory.getName().replaceAll("§", "&") + blockSep;
/*  24 */     ItemStack[] items = inventory.getContents();
/*  25 */     for (int i = 0; i < Math.round((inventory.getSize() / 9)) * 9; i++) {
/*  26 */       ItemStack item = items[i];
/*  27 */       if (item != null && 
/*  28 */         item.getType() != Material.AIR) {
/*  29 */         serInv = String.valueOf(serInv) + "@w" + sep + i; int k;
/*  30 */         for (k = 0; k < (Material.values()).length; k++) {
/*  31 */           if (Material.values()[k].equals(item.getType()))
/*  32 */             serInv = String.valueOf(serInv) + "@m" + sep + k; 
/*  33 */         }  serInv = String.valueOf(serInv) + "@a" + sep + item.getAmount();
/*  34 */         if (item.getDurability() != 0)
/*  35 */           serInv = String.valueOf(serInv) + "@d" + sep + item.getDurability(); 
/*  36 */         if (item.hasItemMeta()) {
/*  37 */           if (item.getItemMeta().hasDisplayName())
/*  38 */             serInv = String.valueOf(serInv) + "@dn" + sep + item.getItemMeta().getDisplayName().replaceAll("§", "&"); 
/*  39 */           if (item.getItemMeta().hasLore()) {
/*  40 */             serInv = String.valueOf(serInv) + "@l" + sep + ((String)item.getItemMeta().getLore().get(0)).replaceAll("§", "&");
/*  41 */             for (k = 1; k < item.getItemMeta().getLore().size(); k++) {
/*  42 */               serInv = String.valueOf(serInv) + sep + ((String)item.getItemMeta().getLore().get(k)).replaceAll("§", "&");
/*     */             }
/*     */           } 
/*  45 */           if (item.getItemMeta().hasEnchants()) {
/*  46 */             serInv = String.valueOf(serInv) + "@e" + sep;
/*  47 */             for (Map.Entry<Enchantment, Integer> ench : (Iterable<Map.Entry<Enchantment, Integer>>)item.getEnchantments().entrySet()) {
/*  48 */               for (int j = 0; j < (Enchantment.values()).length; j++) {
/*  49 */                 if (Enchantment.values()[j].equals(ench.getKey())) {
/*  50 */                   if ((Enchantment.values()).length - j > 1) {
/*  51 */                     serInv = String.valueOf(serInv) + j + "<>" + ench.getValue() + sep;
/*     */                   } else {
/*  53 */                     serInv = String.valueOf(serInv) + j + "<>" + ench.getValue();
/*     */                   } 
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*  60 */         serInv = String.valueOf(serInv) + blockSep;
/*     */       } 
/*     */     } 
/*     */     
/*  64 */     return serInv;
/*     */   }
/*     */   
/*     */   public static Inventory stringToInv(String string) {
/*  68 */     String[] blocks = string.split(blockSep);
/*  69 */     Inventory ser = Bukkit.createInventory(null, Integer.valueOf(blocks[0]).intValue() * 9, blocks[1].replaceAll("&", "§"));
/*  70 */     for (int j = 2; j < blocks.length; j++) {
/*  71 */       String[] itemAttributes = blocks[j].split("@");
/*  72 */       ItemStack item = null;
/*  73 */       int where = 0;
/*  74 */       for (int i = 0; i < itemAttributes.length; i++) {
/*  75 */         String[] attribute = itemAttributes[i].split(sep);
/*  76 */         if (attribute[0].equals("w")) {
/*  77 */           where = Integer.valueOf(attribute[1]).intValue();
/*  78 */         } else if (attribute[0].equals("m")) {
/*  79 */           item = new ItemStack(Material.values()[Integer.valueOf(attribute[1]).intValue()], 1);
/*  80 */         } else if (attribute[0].equals("a")) {
/*  81 */           item.setAmount(Integer.valueOf(attribute[1]).intValue());
/*  82 */         } else if (attribute[0].equals("d")) {
/*  83 */           item.setDurability(Short.valueOf(attribute[1]).shortValue());
/*  84 */         } else if (attribute[0].equals("dn")) {
/*  85 */           ItemMeta itemMeta = item.getItemMeta();
/*  86 */           itemMeta.setDisplayName(attribute[1].replaceAll("&", "§"));
/*  87 */           item.setItemMeta(itemMeta);
/*  88 */         } else if (attribute[0].equals("l")) {
/*  89 */           ItemMeta itemMeta = item.getItemMeta();
/*  90 */           ArrayList<String> lores = new ArrayList<>();
/*  91 */           for (int k = 1; k < attribute.length; k++) {
/*  92 */             lores.add(attribute[k].replaceAll("&", "§"));
/*     */           }
/*  94 */           itemMeta.setLore(lores);
/*  95 */           item.setItemMeta(itemMeta);
/*  96 */         } else if (attribute[0].equals("e")) {
/*  97 */           ItemMeta itemMeta = item.getItemMeta();
/*  98 */           for (int k = 1; k < attribute.length; k++) {
/*  99 */             String[] spe = attribute[k].split("<>");
/* 100 */             itemMeta.addEnchant(Enchantment.values()[Integer.valueOf(spe[0]).intValue()], Integer.valueOf(spe[1]).intValue(), true);
/*     */           } 
/* 102 */           item.setItemMeta(itemMeta);
/*     */         } 
/*     */       } 
/* 105 */       ser.setItem(where, item);
/*     */     } 
/* 107 */     return ser;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void earseArmor(FileConfiguration config, String initialPath, File configFile) {
/* 112 */     config.set(initialPath, null);
/*     */     
/*     */     try {
/* 115 */       config.save(configFile);
/* 116 */     } catch (IOException e) {
/* 117 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void writeArmor(ItemStack[] contents, FileConfiguration config, String initialPath, File configfile) {
/* 123 */     earseArmor(config, initialPath, configfile);
/*     */     
/* 125 */     config.set(String.valueOf(initialPath) + ".boots", contents[0]);
/* 126 */     config.set(String.valueOf(initialPath) + ".leggings", contents[1]);
/* 127 */     config.set(String.valueOf(initialPath) + ".chestplate", contents[2]);
/* 128 */     config.set(String.valueOf(initialPath) + ".helmet", contents[3]);
/*     */     
/*     */     try {
/* 131 */       config.save(configfile);
/* 132 */     } catch (IOException e) {
/* 133 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static ItemStack[] readArmor(FileConfiguration config, String initialPath) {
/* 140 */     ItemStack[] contents = new ItemStack[4];
/*     */     
/* 142 */     contents[0] = config.getItemStack(String.valueOf(initialPath) + ".boots");
/* 143 */     contents[1] = config.getItemStack(String.valueOf(initialPath) + ".leggings");
/* 144 */     contents[2] = config.getItemStack(String.valueOf(initialPath) + ".chestplate");
/* 145 */     contents[3] = config.getItemStack(String.valueOf(initialPath) + ".helmet");
/*     */     
/* 147 */     return contents;
/*     */   }
/*     */ }