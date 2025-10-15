package net.sashakyotoz.wrathy_armament.blocks.entities.recipes;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.Set;

public class WorldshardWorkbenchRecipe implements Recipe<SimpleCraftingContainer> {
    private final int width;
    private final int height;
    private final NonNullList<Ingredient> input;
    private final ResourceLocation id;
    private final ItemStack output;

    public WorldshardWorkbenchRecipe(ResourceLocation id, int width, int height,
                                     NonNullList<Ingredient> recipeItems, ItemStack output) {
        this.width = width;
        this.height = height;
        this.id = id;
        this.input = recipeItems;
        this.output = output;
    }

    @Override
    public boolean matches(SimpleCraftingContainer pContainer, Level pLevel) {
        for (int i = 0; i <= pContainer.getWidth() - this.width; i++) {
            for (int j = 0; j <= pContainer.getHeight() - this.height; j++) {
                if (this.matchesPattern(pContainer, i, j, true))
                    return true;
                if (this.matchesPattern(pContainer, i, j, false))
                    return true;
            }
        }
        return false;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> completeInput = NonNullList.create();
        for (int i = 0; i < input.size(); i++)
            completeInput.add(i, input.get(i));
        return completeInput;
    }

    @Override
    public ItemStack assemble(SimpleCraftingContainer pContainer, RegistryAccess pRegistryAccess) {
        return this.output.copy();
    }
    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return this.output.copy();
    }

    private boolean matchesPattern(SimpleCraftingContainer inv, int offsetX, int offsetY, boolean flipped) {
        for (int i = 0; i < inv.getWidth(); i++) {
            for (int j = 0; j < inv.getHeight(); j++) {
                int k = i - offsetX;
                int l = j - offsetY;
                Ingredient ingredient = Ingredient.EMPTY;
                if (k >= 0 && l >= 0 && k < this.width && l < this.height) {
                    if (flipped)
                        ingredient = this.input.get(this.width - k - 1 + l * this.width);
                    else
                        ingredient = this.input.get(k + l * this.width);
                }
                if (!ingredient.test(inv.getItem(i + j * inv.getWidth()))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return width >= this.width && height >= this.height;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    static NonNullList<Ingredient> dissolvePattern(String[] pPattern, Map<String, Ingredient> pKeys, int pPatternWidth, int pPatternHeight) {
        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(pPatternWidth * pPatternHeight, Ingredient.EMPTY);
        Set<String> set = Sets.newHashSet(pKeys.keySet());
        set.remove(" ");

        for (int i = 0; i < pPattern.length; ++i) {
            for (int j = 0; j < pPattern[i].length(); ++j) {
                String s = pPattern[i].substring(j, j + 1);
                Ingredient ingredient = pKeys.get(s);
                if (ingredient == null) {
                    throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
                }

                set.remove(s);
                nonnulllist.set(j + pPatternWidth * i, ingredient);
            }
        }

        if (!set.isEmpty()) {
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
        } else {
            return nonnulllist;
        }
    }

    @VisibleForTesting
    static String[] shrink(String... pToShrink) {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;

        for (int i1 = 0; i1 < pToShrink.length; ++i1) {
            String s = pToShrink[i1];
            i = Math.min(i, firstNonSpace(s));
            int j1 = lastNonSpace(s);
            j = Math.max(j, j1);
            if (j1 < 0) {
                if (k == i1) {
                    ++k;
                }

                ++l;
            } else {
                l = 0;
            }
        }

        if (pToShrink.length == l) {
            return new String[0];
        } else {
            String[] astring = new String[pToShrink.length - l - k];

            for (int k1 = 0; k1 < astring.length; ++k1) {
                astring[k1] = pToShrink[k1 + k].substring(i, j + 1);
            }

            return astring;
        }
    }

    public boolean isIncomplete() {
        NonNullList<Ingredient> nonnulllist = this.getIngredients();
        return nonnulllist.isEmpty() || nonnulllist.stream().filter((ingredient) -> !ingredient.isEmpty()).anyMatch((p_151273_) -> net.minecraftforge.common.ForgeHooks.hasNoElements(p_151273_));
    }

    private static int firstNonSpace(String pEntry) {
        int i;
        for (i = 0; i < pEntry.length() && pEntry.charAt(i) == ' '; ++i) {
        }

        return i;
    }

    private static int lastNonSpace(String pEntry) {
        int i;
        for (i = pEntry.length() - 1; i >= 0 && pEntry.charAt(i) == ' '; --i) {
        }

        return i;
    }

    static String[] patternFromJson(JsonArray pPatternArray) {
        String[] astring = new String[pPatternArray.size()];
        if (astring.length > 3) {
            throw new JsonSyntaxException("Invalid pattern: too many rows, " + 3 + " is maximum");
        } else if (astring.length == 0) {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        } else {
            for (int i = 0; i < astring.length; ++i) {
                String s = GsonHelper.convertToString(pPatternArray.get(i), "pattern[" + i + "]");
                if (s.length() > 3) {
                    throw new JsonSyntaxException("Invalid pattern: too many columns, " + 3 + " is maximum");
                }

                if (i > 0 && astring[0].length() != s.length()) {
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                }

                astring[i] = s;
            }

            return astring;
        }
    }

    /**
     * Returns a key json object as a Java HashMap.
     */
    static Map<String, Ingredient> keyFromJson(JsonObject pKeyEntry) {
        Map<String, Ingredient> map = Maps.newHashMap();

        for (Map.Entry<String, JsonElement> entry : pKeyEntry.entrySet()) {
            if (entry.getKey().length() != 1) {
                throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            }

            if (" ".equals(entry.getKey())) {
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            }

            map.put(entry.getKey(), Ingredient.fromJson(entry.getValue(), false));
        }

        map.put(" ", Ingredient.EMPTY);
        return map;
    }

    public static ItemStack itemStackFromJson(JsonObject pStackObject) {
        return CraftingHelper.getItemStack(pStackObject, true, true);
    }

    public static Item itemFromJson(JsonObject pItemObject) {
        String s = GsonHelper.getAsString(pItemObject, "item");
        Item item = ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(s));
        if (item == Items.AIR)
            throw new JsonSyntaxException("Empty ingredient not allowed here");
        else
            return item;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<WorldshardWorkbenchRecipe> {
        private Type() {
        }

        public static final Type INSTANCE = new Type();
    }

    public static class Serializer implements RecipeSerializer<WorldshardWorkbenchRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        public WorldshardWorkbenchRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            Map<String, Ingredient> map = WorldshardWorkbenchRecipe.keyFromJson(GsonHelper.getAsJsonObject(pJson, "key"));
            String[] astring = WorldshardWorkbenchRecipe.shrink(WorldshardWorkbenchRecipe.patternFromJson(GsonHelper.getAsJsonArray(pJson, "pattern")));
            int i = astring[0].length();
            int j = astring.length;
            NonNullList<Ingredient> nonnulllist = WorldshardWorkbenchRecipe.dissolvePattern(astring, map, i, j);
            ItemStack itemstack = WorldshardWorkbenchRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pJson, "result"));
            return new WorldshardWorkbenchRecipe(pRecipeId, i, j, nonnulllist, itemstack);
        }

        public WorldshardWorkbenchRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            int i = pBuffer.readVarInt();
            int j = pBuffer.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);
            nonnulllist.replaceAll(ignored -> Ingredient.fromNetwork(pBuffer));
            ItemStack itemstack = pBuffer.readItem();
            return new WorldshardWorkbenchRecipe(pRecipeId, i, j, nonnulllist, itemstack);
        }

        public void toNetwork(FriendlyByteBuf pBuffer, WorldshardWorkbenchRecipe pRecipe) {
            pBuffer.writeVarInt(pRecipe.width);
            pBuffer.writeVarInt(pRecipe.height);
            for (Ingredient ingredient : pRecipe.getIngredients()) {
                ingredient.toNetwork(pBuffer);
            }
            pBuffer.writeItem(pRecipe.output);
        }
    }
}