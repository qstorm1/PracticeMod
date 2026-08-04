package com.qstorm.item;




public class ModItems {
//    public static final Item SOUL_ORE = registerItem("soul_ore",new Item.Settings(),Item.class);
//
//    public static final Item RAW_SOUL_ORE = registerItem("raw_soul_ore",new Item.Settings(),Item.class);
//
//    public static final Item CHISEl = registerItem("chisel",new Item.Settings(),Chizel.class);
//
//
//
//    //used AI to help with class and constructor stuff
//    public static<T extends Item> Item registerItem(String name,Item.Settings itemSettings,Class<T> classT){
//        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM,Identifier.of(PracticeMod.MOD_ID,name));
//        try {
//            //this creates an item assuming the item has the same constructors as it's parent
//            T item = classT.getDeclaredConstructor(Item.Settings.class).newInstance(itemSettings.registryKey(key));
//            return Registry.register(Registries.ITEM, key, item);
//        }
//        catch(Exception e){
//            System.out.println("error loading object: "+name);
//            PracticeMod.LOGGER.info("error loading object {}", name);
//        }
//
//        return Registry.register(Registries.ITEM, key, new Item(itemSettings));
//
//
//    }
//
//
//
//    public static void registerModItems(){
//        PracticeMod.LOGGER.info("Registering All Mods");
//
//        //add item to item group
//        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(fabricItemGroupEntries -> {
//            fabricItemGroupEntries.add(SOUL_ORE);
//            fabricItemGroupEntries.add(RAW_SOUL_ORE);
//            fabricItemGroupEntries.add(CHISEl);
//        });
//
//        PracticeMod.LOGGER.info("Registered Mods!");
//    }

}
