package io.nickw.game.item;

public class ItemRegistry {
	public static ItemStack[] items = new ItemStack[255];

	public static int getId(ItemStack item) {
		for (int i = 0; i < items.length; i++) {
			if (items[i].matches(item)) {
				return i;
			}
		}
		return -1;
	}
}