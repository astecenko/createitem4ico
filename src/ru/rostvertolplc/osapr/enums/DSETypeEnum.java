package ru.rostvertolplc.osapr.enums;

 public enum DSETypeEnum
 {
   empty("Неизвестный тип", ""), 
   H47Detal("Деталь", "H47_Detal"), 
   H47Komplect("Комплект", "H47_Complect"), 
   H47SE("Cб. ед.", "H47_SE"), 
   H47Document("Документ", "H47_Document");
   
   private String name;
   private String type;
   
   private DSETypeEnum(String name, String type) {
     this.name = name;
     this.type = type;
   }
   
   public String getName() {
     return this.name;
   }
   
   public String getType() {
     return this.type;
   }
   
 
 
   public String toString() { return getName(); }
   
   public static String getTypeByName(String name) {
     DSETypeEnum[] arrayOfDSETypeEnum;
     int j = (arrayOfDSETypeEnum = values()).length; for (int i = 0; i < j; i++) { DSETypeEnum v = arrayOfDSETypeEnum[i];
       if (v.getName().equals(name)) {
         return v.getType();
       }
     }
     return "";
   }
   
   public static DSETypeEnum getEnumByName(String name) { DSETypeEnum[] arrayOfDSETypeEnum;
     int j = (arrayOfDSETypeEnum = values()).length; for (int i = 0; i < j; i++) { DSETypeEnum v = arrayOfDSETypeEnum[i];
       if (v.getName().equals(name)) {
         return v;
       }
     }
     return empty;
   }
   
   public static String getNameByType(String type) { DSETypeEnum[] arrayOfDSETypeEnum;
     int j = (arrayOfDSETypeEnum = values()).length; for (int i = 0; i < j; i++) { DSETypeEnum v = arrayOfDSETypeEnum[i];
       if (v.getType().equals(type)) {
         return v.getName();
       }
     }
     return "";
   }
   
   public static DSETypeEnum getEnumByType(String type) { DSETypeEnum[] arrayOfDSETypeEnum;
     int j = (arrayOfDSETypeEnum = values()).length; for (int i = 0; i < j; i++) { DSETypeEnum v = arrayOfDSETypeEnum[i];
       if (v.getType().equals(type)) {
         return v;
       }
     }
     return empty;
   }
 }