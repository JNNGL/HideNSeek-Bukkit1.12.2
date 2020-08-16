/*    */ package com.jnngl.plugin.task;
/*    */ 
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.plugin.Plugin;
/*    */ import org.bukkit.plugin.java.JavaPlugin;
/*    */ import org.bukkit.util.Consumer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Timer
/*    */   implements Runnable
/*    */ {
/*    */   private JavaPlugin plugin;
/*    */   private static Integer assignedTaskId;
/*    */   private int seconds;
/*    */   private int secondsLeft;
/*    */   private Consumer<Timer> everySecond;
/*    */   private Runnable beforeTimer;
/*    */   private Runnable afterTimer;
/*    */   
/*    */   public Timer(JavaPlugin plugin, int seconds, Runnable beforeTimer, Runnable afterTimer, Consumer<Timer> everySecond) {
/* 28 */     this.plugin = plugin;
/*    */     
/* 30 */     this.seconds = seconds;
/* 31 */     this.secondsLeft = seconds;
/* 32 */     this.beforeTimer = beforeTimer;
/* 33 */     this.afterTimer = afterTimer;
/* 34 */     this.everySecond = everySecond;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void run() {
/* 40 */     if (this.secondsLeft < 1) {
/*    */       
/* 42 */       this.afterTimer.run();
/*    */       
/* 44 */       if (assignedTaskId != null) {
/* 45 */         Bukkit.getScheduler().cancelTask(assignedTaskId.intValue());
/*    */       }
/*    */       return;
/*    */     } 
/* 49 */     if (this.secondsLeft == this.seconds)
/* 50 */       this.beforeTimer.run(); 
/* 51 */     this.everySecond.accept(this);
/* 52 */     this.secondsLeft--;
/*    */   }
/*    */ 
/*    */   
/*    */   public static void cancel() {
/* 57 */     if (assignedTaskId != null) {
/* 58 */       Bukkit.getScheduler().cancelTask(assignedTaskId.intValue());
/*    */     }
/*    */   }
/*    */   
/*    */   public int getSecondsLeft() {
/* 63 */     return this.secondsLeft;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getTotalSeconds() {
/* 68 */     return this.seconds;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void sheduleTimer() {
/* 74 */     assignedTaskId = Integer.valueOf(Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)this.plugin, this, 0L, 20L));
/*    */   }
/*    */ 
/*    */   
/*    */   public void setSeconds(int s) {
/* 79 */     this.secondsLeft = s;
/*    */   }
/*    */ }