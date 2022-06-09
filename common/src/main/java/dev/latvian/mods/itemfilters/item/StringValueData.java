package dev.latvian.mods.itemfilters.item;

import net.minecraft.core.Registry;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * @author LatvianModder
 */
public abstract class StringValueData<T> {
	public final ItemStack filter;
	protected T value;
	protected boolean load;

	public StringValueData(ItemStack is) {
		filter = is;
		value = null;
		load = true;
	}

	@Nullable
	protected abstract T fromString(String s);

	protected abstract String toString(@Nullable T value);

	public final String toString() {
		return Registry.ITEM.getKey(filter.getItem()) + ":" + toString(getValue());
	}

	@Nullable
	public T getValue() {
		if (load) {
			load = false;
			value = null;

			if (filter.hasTag()) {
				value = fromString(filter.getTag().getString("value"));
			}
		}

		return value;
	}

	public void setValue(@Nullable T v) {
		value = v;
		load = false;
		String s = value == null ? "" : toString(value);

		if (s.isEmpty()) {
			filter.removeTagKey("value");
		} else {
			filter.addTagElement("value", StringTag.valueOf(s));
		}
	}

	public void setValueFromString(String v) {
		setValue(fromString(v));
	}

	public String getValueAsString() {
		return toString(getValue());
	}

	public Component getValueAsComponent() {
		T v = getValue();

		if (v == null) {
			return Component.empty();
		}

		String s = toString(v);
		return s.isEmpty() ? Component.empty() : Component.literal(s);
	}
}