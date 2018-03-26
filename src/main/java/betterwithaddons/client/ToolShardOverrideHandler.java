package betterwithaddons.client;

import betterwithaddons.item.ModItems;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.HashMap;

public class ToolShardOverrideHandler extends ItemOverrideList
{
    public static final ToolShardOverrideHandler INSTANCE = new ToolShardOverrideHandler();
    private HashMap<Item,IBakedModel> brokenModels = new HashMap<>();

    public ToolShardOverrideHandler() {
        super(ImmutableList.<ItemOverride>of());
    }

    public void addModel(Item item, IBakedModel model)
    {
        brokenModels.put(item,model);
    }

    @Override
    public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity)
    {
        ItemStack innerstack = ModItems.brokenArtifact.getInnerStack(stack);

        if (!stack.isEmpty() && !innerstack.isEmpty())
        {
            Item item = innerstack.getItem();
            if(brokenModels.containsKey(item)) {
                return brokenModels.get(item);
            }
            else
            {
                ResourceLocation location = applyOverride(innerstack, world, entity);
                if (location != null) {
                    return net.minecraft.client.Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getModel(net.minecraftforge.client.model.ModelLoader.getInventoryVariant(location.toString()));
                }
            }
        }
        return originalModel;
    }
}