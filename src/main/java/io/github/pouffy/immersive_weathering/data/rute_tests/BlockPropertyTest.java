package io.github.pouffy.immersive_weathering.data.rute_tests;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pouffy.immersive_weathering.data.block_growths.Operator;
import io.github.pouffy.immersive_weathering.reg.ModRuleTests;
import io.github.pouffy.immersive_weathering.util.StrOpt;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class BlockPropertyTest extends RuleTest {
    public static final MapCodec<BlockPropertyTest> CODEC = PropPredicate.CODEC.listOf().fieldOf("properties")
            .xmap(BlockPropertyTest::new, (t) -> t.propPredicates);

    private final List<PropPredicate> propPredicates;

    private BlockPropertyTest(List<PropPredicate> propPredicates) {
        this.propPredicates = propPredicates;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean test(BlockState state, RandomSource random) {
        for(var p : propPredicates){
            if(!p.test(state))return false;
        }
        return true;
    }

    @Override
    protected @NotNull RuleTestType<BlockPropertyTest> getType() {
        return ModRuleTests.BLOCK_PROPERTY_TEST.get();
    }


    private static final class PropPredicate implements Predicate<BlockState> { //

        public static Codec<PropPredicate> CODEC = RecordCodecBuilder.create(i -> i.group(
                BuiltInRegistries.BLOCK.byNameCodec().fieldOf("from_block").forGetter(PropPredicate::getFromBlock),
                Codec.STRING.fieldOf("property").forGetter(p -> p.property.getName()),
                StrOpt.of(Codec.STRING, "value").forGetter(p -> {
                    if (p.targetValue != null) {
                        return Optional.of(p.targetValue.toString());
                    }
                    return Optional.empty();
                }),
                StrOpt.of(Operator.CODEC, "operator", Operator.EQUAL).forGetter(PropPredicate::getOperator)
        ).apply(i, (block, propertyName, valueStr, operator) -> {
            Property<?> property = null;
            
            // Find the property by name
            for (var p : block.defaultBlockState().getProperties()) {
                if (p.getName().equals(propertyName)) {
                    property = p;
                    break;
                }
            }
            
            if (property == null) {
                throw new IllegalArgumentException("Property " + propertyName + " not found in block " + block);
            }
            
            // Parse the value if provided
            Optional<Comparable<?>> value = Optional.empty();
            if (valueStr.isPresent() && !valueStr.get().isEmpty()) {
                @SuppressWarnings("unchecked")
                Optional<Comparable<?>> parsed = (Optional<Comparable<?>>) property.getValue(valueStr.get());
                value = parsed;
            }
            
            return new PropPredicate(block, property, value, operator);
        }));

        private final Block fromBlock;
        private final Property<?> property;
        private final Operator operator;

        @Nullable
        private final Comparable<?> targetValue;
        private final Integer intValue;


        @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
        public PropPredicate(Block fromBlock, Property<?> property, Optional<Comparable<?>> value, Operator operator) {
            this.property = property;
            this.targetValue = value.orElse(null);
            this.fromBlock = fromBlock;
            this.operator = operator;
            if (property instanceof IntegerProperty && targetValue instanceof Integer i) {
                intValue = i;
            } else intValue = null;
        }

        public Block getFromBlock() {
            return fromBlock;
        }

        public Optional<Comparable<?>> getTargetValue() {
            return Optional.ofNullable(targetValue);
        }

        public Property<?> getProperty() {
            return property;
        }

        public Operator getOperator() {
            return operator;
        }

        @Override
        public boolean test(BlockState state) {
            var val = state.getOptionalValue(property);
            if (val.isPresent()) {
                if (intValue != null) {
                    return operator.apply((Integer)val.get(),  intValue);
                }
                return targetValue == null || val.get() == targetValue;
            }
            return false;
        }

    }

}
