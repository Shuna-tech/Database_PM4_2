package pm4.tools;

import pm4.dal.*;
import pm4.model.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Driver {
    public static void main(String[] args) throws SQLException {
        // Create DAOs
        PlayersDao playersDao = PlayersDao.getInstance();
        CharacterInfoDao characterInfoDao = CharacterInfoDao.getInstance();
        JobsDao jobsDao = JobsDao.getInstance();
        CharacterJobsDao characterJobsDao = CharacterJobsDao.getInstance();
        AttributesDao attributesDao = AttributesDao.getInstance();
        CharacterAttributesDao characterAttributesDao = CharacterAttributesDao.getInstance();
        ItemsDao itemsDao = ItemsDao.getInstance();
        EquipmentSlotsDao slotsDao = EquipmentSlotsDao.getInstance();
        EquippableItemsDao equippableItemsDao = EquippableItemsDao.getInstance();
        WeaponsDao weaponsDao = WeaponsDao.getInstance();
        GearsDao gearsDao = GearsDao.getInstance();
        CurrenciesDao currenciesDao = CurrenciesDao.getInstance();
        CharacterCurrenciesDao characterCurrenciesDao = CharacterCurrenciesDao.getInstance();
        CharacterEquipmentsDao characterEquipmentsDao = CharacterEquipmentsDao.getInstance();
        ConsumablesDao consumablesDao = ConsumablesDao.getInstance();
        ConsumableAttributesDao consumableAttributesDao = ConsumableAttributesDao.getInstance();
        GearAndWeaponAttributesDao gearAndWeaponAttributesDao = GearAndWeaponAttributesDao.getInstance();
        GearAndWeaponJobsDao gearAndWeaponJobsDao = GearAndWeaponJobsDao.getInstance();
        InventoryPositionsDao inventoryPositionsDao = InventoryPositionsDao.getInstance();
        
        try {
            // Create 10 Players
            List<Players> players = new ArrayList<>();
            String[] userNames = {"DragonSlayer", "MageSupreme", "SwordMaster", "HealerPro", "RogueShadow", 
                                "WarriorKing", "ArcherElite", "PaladinHoly", "NinjaMaster", "BerserkerAxe"};
            
            for (String userName : userNames) {
                Players player = playersDao.create(new Players(userName, userName.toLowerCase() + "@email.com"));
                players.add(player);
                System.out.println("Created player: " + player.getUserName());
            }

            // Create Characters
            List<CharacterInfo> characters = new ArrayList<>();
            String[] firstNames = {"Arthur", "Merlin", "Lancelot", "Gwen", "Robin", "Conan", "Legolas", "Galahad", "Hanzo", "Thor"};
            String[] lastNames = {"Pendragon", "Wizard", "Knight", "Healer", "Hood", "Warrior", "Greenleaf", "Pure", "Shadow", "Thunder"};
            int[] maxHPs = {1000, 800, 1200, 700, 900, 1500, 850, 1100, 750, 1300};

            for (int i = 0; i < 10; i++) {
                CharacterInfo character = characterInfoDao.create(
                    new CharacterInfo(firstNames[i], lastNames[i], maxHPs[i], players.get(i))
                );
                characters.add(character);
                System.out.println("Created character: " + character.getFirstName());
            }

            // Create Jobs
            List<Jobs> jobs = new ArrayList<>();
            String[] jobNames = {"Warrior", "Mage", "Healer", "Rogue", "Archer", 
                               "Paladin", "Berserker", "Ninja", "Summoner", "Dark Knight"};
            
            for (String jobName : jobNames) {
                Jobs job = jobsDao.create(new Jobs(jobName));
                jobs.add(job);
                System.out.println("Created job: " + job.getJobName());
            }

            // Create Character Jobs
            for (int i = 0; i < 10; i++) {
                CharacterJobs characterJob = characterJobsDao.create(
                    new CharacterJobs(characters.get(i), jobs.get(i % jobs.size()), 
                                    40 + i, 80000 + (i * 2000), true)
                );
                System.out.println("Created character job: " + characterJob.getJob().getJobName());
            }

            // Create Attributes
            List<Attributes> attributes = new ArrayList<>();
            String[] attrNames = {"Strength", "Intelligence", "Dexterity", "Vitality", "Mind",
                                "Spirit", "Tenacity", "Critical Hit", "Determination", "Direct Hit Rate"};
            
            for (String attrName : attrNames) {
                Attributes attribute = attributesDao.create(new Attributes(attrName));
                attributes.add(attribute);
                System.out.println("Created attribute: " + attribute.getAttributeName());
            }

            // Create Character Attributes
            for (int i = 0; i < 10; i++) {
                CharacterAttributes characterAttr = characterAttributesDao.create(
                    new CharacterAttributes(attributes.get(i), characters.get(i), 260 + (i * 10))
                );
                System.out.println("Created character attribute: " + characterAttr.getAttributes().getAttributeName());
            }

            // Create Equipment Slots
            List<EquipmentSlots> slots = new ArrayList<>();
            String[] slotNames = {"Main Hand", "Off Hand", "Head", "Body", "Hands",
                                "Legs", "Feet", "Neck", "Ears", "Wrists"};
            
            for (String slotName : slotNames) {
                EquipmentSlots slot = slotsDao.create(new EquipmentSlots(slotName));
                slots.add(slot);
                System.out.println("Created equipment slot: " + slot.getSlotName());
            }

            // Create Items and Equipment
            List<Items> items = new ArrayList<>();
            List<EquippableItems> equippableItems = new ArrayList<>();
            String[] itemNames = {"Excalibur", "Staff of Power", "Holy Shield", "Healing Rod", "Bow of Shadows",
                                "Battle Axe", "Elven Bow", "Sacred Sword", "Ninja Blades", "Thunder Hammer"};
            
            for (int i = 0; i < 10; i++) {
                Items item = itemsDao.create(new Items(itemNames[i], 1, true, 35000 + (i * 1500)));
                System.out.println("Created item with id: " + item.getItemID());

                items.add(item);
                
                // Create EquippableItems
                EquippableItems equippable = equippableItemsDao.create(
                    new EquippableItems(item.getItemID(), item.getItemName(), item.getMaxStackSize(),
                                      item.isMarketAllowed(), item.getVendorPrice(),
                                      50, slots.get(0), 50)
                );
                System.out.println("Created equippable item with id: " + equippable.getItemID());

                equippableItems.add(equippable);
                
                // Create Weapons for first 8 items
                if (i < 8) {
                    Weapons weapon = weaponsDao.create(
                        new Weapons(equippable.getItemID(), equippable.getItemName(),
                                  equippable.getMaxStackSize(), equippable.isMarketAllowed(),
                                  equippable.getVendorPrice(), equippable.getItemLevel(),
                                  slots.get(0), equippable.getRequiredLevel(),
                                  100 + i * 5, 85.5 + i * 2.0, 2.6 + (i * 0.1))
                    );
                    System.out.println("Created weapon: " + weapon.getItemName());
                } else {  // Create Gears for last 2 items
                    Gears gear = gearsDao.create(
                        new Gears(equippable.getItemID(), equippable.getItemName(),
                                equippable.getMaxStackSize(), equippable.isMarketAllowed(),
                                equippable.getVendorPrice(), equippable.getItemLevel(),
                                slots.get(i % 5 + 1), equippable.getRequiredLevel(),
                                90 + i * 10, 70 + i * 10)
                    );
                    System.out.println("Created gear: " + gear.getItemName());
                }

                // Create GearAndWeaponAttributes for each item
                GearAndWeaponAttributes itemAttr = gearAndWeaponAttributesDao.create(
                    new GearAndWeaponAttributes(equippable, attributes.get(i), 50 + i * 5)
                );
                System.out.println("Created gear/weapon attribute for: " + item.getItemName());

                // Create GearAndWeaponJobs for each item
                GearAndWeaponJobs itemJob = gearAndWeaponJobsDao.create(
                    new GearAndWeaponJobs(equippable, jobs.get(i))
                );
                System.out.println("Created gear/weapon job requirement for: " + item.getItemName());
            }

            // Create Consumables (using the same items)
            for (int i = 0; i < 10; i++) {
                Consumables consumable = consumablesDao.create(
                    new Consumables(items.get(i).getItemID(), 40 + i, 
                                  "Consumable effect for " + items.get(i).getItemName())
                );
                
                // Create ConsumableAttributes
                ConsumableAttributes consumableAttr = consumableAttributesDao.create(
                    new ConsumableAttributes(items.get(i), attributes.get(i), 
                                          80 + i * 5, 0.25f + (i * 0.05f))
                );
                System.out.println("Created consumable: " + consumable.getDescription());
            }

         // Create Currencies
            List<Currencies> currencies = new ArrayList<>();
            String[] currencyNames = {"Gil", "Tomestones", "Seals", "Marks", "Gems",
                                    "Crystals", "Shards", "Scrips", "Tokens", "Points"};
            int[] weeklyCaps = {0, 450, 450, 500, 1000, 2000, 1500, 3000, 2500, 1000}; 

            for (int i = 0; i < 10; i++) {
                Currencies currency = currenciesDao.create(
                    new Currencies(currencyNames[i], 999999, weeklyCaps[i])
                );
                currencies.add(currency);
                System.out.println("Created currency: " + currency.getCurrencyName());
            }

         // Create Character Currencies
            for (int i = 0; i < 10; i++) {
                CharacterCurrencies charCurrency = characterCurrenciesDao.create(
                    new CharacterCurrencies(characters.get(i), currencies.get(i),
                        10000 + (i * 1000),
                        200 + (i * 50))  // Set actual weekly amount for all
                );
          
            }

            // Create Character Equipments
            for (int i = 0; i < 10; i++) {
                CharacterEquipments equipment = characterEquipmentsDao.create(
                    new CharacterEquipments(characters.get(i), slots.get(i % slots.size()),
                                          equippableItemsDao.getById(equippableItems.get(i).getItemID()))
                );
                
            	
                System.out.println("Created character equipment for: " + 
                        equipment.getCharacter() + ", item: " + equipment.getItem());
                System.out.println("equippableItem id: " + 
                		equippableItems.get(i).getItemID());
                System.out.println("item id: " + 
                		items.get(i).getItemID());
                
                //System.out.println("Created character equipment for: " + 
//                                 equipment.getCharacterInfo().getFirstName());
            }

            // Create Inventory Positions
            for (int i = 0; i < 10; i++) {
                InventoryPositions inventory = inventoryPositionsDao.create(
                    new InventoryPositions(characters.get(i), i, items.get(i), 1)
                );
                System.out.println("Created inventory position for: " + 
                                 inventory.getCharacterInfo().getFirstName());
            }

            System.out.println("Sample data insertion completed successfully!");
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
