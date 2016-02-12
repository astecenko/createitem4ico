package ru.rostvertolplc.osapr.utils;

public class Transliterator
 {
   static String ARus = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя\\/";

   static String[] ALat = { "A", "B", "V", "G", "D", "E", "YO", "ZH", "Z", 
     "I", "J", "K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "F", 
     "X", "C", "CH", "SH", "SHH", "'", "Y", "`", "E", "YU", "YA", "a", 
     "b", "v", "g", "d", "e", "yo", "zh", "z", "i", "j", "k", "l", "m", 
     "n", "o", "p", "r", "s", "t", "u", "f", "x", "c", "ch", "sh", 
     "shh", "'", "y", "`", "e", "yu", "ya", "^", "^" };
    
   public static String Decode_RUS_LAT(String sRus)
   {
     StringBuffer Result = new StringBuffer();
     int n = sRus.length();
     for (int i = 0; i < n; i++) {
       char ch = sRus.charAt(i);
       int j = ARus.indexOf(ch);
       if (j >= 0) {
         Result.append(ALat[j]);
       } else
         Result.append(ch);
     }
     return Result.toString();
   }
 }