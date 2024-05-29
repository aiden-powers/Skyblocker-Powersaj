package de.hysky.skyblocker.skyblock.item.tooltip;

import de.hysky.skyblocker.skyblock.item.MuseumItemCache;
import de.hysky.skyblocker.utils.ItemUtils;
import de.hysky.skyblocker.utils.tooltip.TooltipAdder;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class MuseumTooltip extends TooltipAdder {
	public MuseumTooltip(int priority) {
		super(priority);
	}

	@Override
	public void addToTooltip(List<Text> lore, Slot focusedSlot) {
		final ItemStack itemStack = focusedSlot.getStack();
		final String internalID = ItemTooltip.getInternalNameFromNBT(itemStack, true);
		if (TooltipInfoType.MUSEUM.isTooltipEnabledAndHasOrNullWarning(internalID)) {
			String itemCategory = TooltipInfoType.MUSEUM.getData().get(internalID).getAsString();
			String format = switch (itemCategory) {
				case "Weapons" -> "%-18s";
				case "Armor" -> "%-19s";
				default -> "%-20s";
			};

			//Special case the special category so that it doesn't always display not donated
			if (itemCategory.equals("Special")) {
				lore.add(Text.literal(String.format(format, "Museum: (" + itemCategory + ")"))
				             .formatted(Formatting.LIGHT_PURPLE));
			} else {
				NbtCompound customData = ItemUtils.getCustomData(itemStack);
				boolean isInMuseum = (customData.contains("donated_museum") && customData.getBoolean("donated_museum")) || MuseumItemCache.hasItemInMuseum(internalID);

				Formatting donatedIndicatorFormatting = isInMuseum ? Formatting.GREEN : Formatting.RED;

				lore.add(Text.literal(String.format(format, "Museum (" + itemCategory + "):"))
				             .formatted(Formatting.LIGHT_PURPLE)
				             .append(Text.literal(isInMuseum ? "✔" : "✖").formatted(donatedIndicatorFormatting, Formatting.BOLD))
				             .append(Text.literal(isInMuseum ? " Donated" : " Not Donated").formatted(donatedIndicatorFormatting)));
			}
		}
	}
}
