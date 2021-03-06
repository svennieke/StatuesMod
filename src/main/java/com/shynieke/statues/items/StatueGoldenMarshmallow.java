package com.shynieke.statues.items;

import com.shynieke.statues.init.StatueFoods;
import com.shynieke.statues.init.StatueRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;

public class StatueGoldenMarshmallow extends Item {
    public StatueGoldenMarshmallow(Properties builder) {
        super(builder.food(StatueFoods.GOLDEN_MARSHMALLOW));
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityIn) {
        if(this.isFood()) {
            if (!worldIn.isRemote) {
                if (this == StatueRegistry.MARSHMALLOW_GOLDEN.get()) {
                    ArrayList<Effect> potionList = new ArrayList<>(ForgeRegistries.POTIONS.getValues());
                    potionList.remove(Effects.NAUSEA);

                    int i = random.nextInt(potionList.size());
                    int amplifier = random.nextInt(2);
                    Effect randomPotion = potionList.get(i);
                    EffectInstance randomEffect = new EffectInstance(randomPotion, 200, amplifier);
                    entityIn.addPotionEffect(randomEffect);
                }
            }
            return entityIn.onFoodEaten(worldIn, stack);
        }

        return stack;
    }

    @Override
    public boolean hasEffect(ItemStack p_77636_1_) {
        return true;
    }
}
