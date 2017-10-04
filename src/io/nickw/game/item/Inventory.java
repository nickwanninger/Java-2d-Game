package io.nickw.game.item;

import io.nickw.game.Game;
import io.nickw.game.entity.Player;
import io.nickw.game.gfx.Color;
import io.nickw.game.gfx.Font;
import io.nickw.game.gfx.Screen;
import io.nickw.game.gfx.SpriteReference;
import io.nickw.game.sound.Sounds;
import io.nickw.game.util.Mouse;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
	public List<ItemStack> items = new ArrayList<ItemStack>();
	private Player player;
	public boolean visible = false;
	public int hoverIndex = -1;
	public int selectedIndex = -1;
	public Inventory(Player player) {
		this.player = player;
	}

	public void add(ItemStack item) {
		add(items.size(), item);
	}

	public void add(int slot, ItemStack item) {
		if (item != null) {
			ItemStack has = findResource(item);
			if (has == null) {
				items.add(slot, item);
			} else {
				has.count += item.count;
			}
		}
	}

	public ItemStack findResource(ItemStack item) {
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i) != null) {
				ItemStack has = items.get(i);
				if (has.matches(item)) return has;
			}
		}
		return null;
	}

	public int getCount(ItemStack item) {
		ItemStack found = findResource(item);
		if (found != null) {
			return found.count;
		} else {
			return 0;
		}
	}

  	public void tick() {
		if (visible) {
			if (Mouse.left.down) {
				Mouse.left.release();

				if (hoverIndex == selectedIndex) {
					Sounds.click.play(0.2f);
					selectedIndex = -1;
				} else if (hoverIndex != -1 && hoverIndex + 1 <= items.size()) {
					Sounds.select.play(0.2f);
					selectedIndex = hoverIndex;
				}
			}
		}
	}

	public void drawUI(Screen screen) {
		ItemStack selectedItem = getSelectedItem();
		if (selectedItem != null) {
			selectedItem.drawInventory(screen, 2, 2);
		} else if (Mouse.left.down) {
			Font.drawText(screen, "no item!", 1, 13, 0xffffff);
		}
	}

	public void render(Screen screen) {

		if (visible) {
			for (int i = 0; i < screen.pixels.length; i++) {
				screen.pixels[i] = 0;
			}
			int inventory_width = 112;
			int inventory_height = 97;
			int tx = Game.WIDTH / 2 - inventory_width / 2;
			int ty = Game.HEIGHT / 2 - inventory_height / 2;
			SpriteReference inventorySprite = new SpriteReference(256, 0, inventory_width, inventory_height);

			screen.drawSpriteGUI(inventorySprite, tx, ty);
			Font.drawText(screen, "Inventory   ", tx + 4, ty + 6, 0x595652);
			int cellSize = 10;
			SpriteReference cellSprite = new SpriteReference(246, 0, cellSize, cellSize);
			SpriteReference cellSpriteSelected = new SpriteReference(246, cellSize, cellSize, cellSize);
			int xx = tx + 5;
			int yy = ty + 15;
			boolean hoveringOverItem = false;
			int cellColumns = 6;
			for (int i = 0; i < 42; i++) {
				int row = i % cellColumns;
				int col = i / cellColumns;
				int dx = row * (cellSize + 1) + xx;
				int dy = col * (cellSize + 1) + yy;
				boolean hovered = screen.mouseWithin(dx, dy, dx + cellSize, dy + cellSize);
				if (hovered) {
					hoveringOverItem = true;
					hoverIndex = i;
				}
				SpriteReference ds = hovered ? cellSpriteSelected : cellSprite;
				if (selectedIndex == i) {
					ds = new SpriteReference(246, cellSize * 2, cellSize, cellSize);
				}
				screen.drawSpriteGUI(ds, dx, dy);
				if (items.size() > i) {
					ItemStack item = items.get(i);
					item.drawInventory(screen, dx + 1, dy + 1);
				}
			}
			if (!hoveringOverItem) {
				hoverIndex = -1;
			}

			if (selectedIndex != -1) {
				ItemStack item = getSelectedItem();
				item.drawInventory(screen, tx + 87, ty + 12);
				Font.drawTextCentered(screen, item.getName(), tx + 89, ty + 34, 0x595652);
			}
		} else {
			drawUI(screen);
		}


	}
	public ItemStack getSelectedItem() {
		if (selectedIndex <= items.size() - 1 && selectedIndex != -1) {
			return items.get(selectedIndex);
		}
		return null;
	}

	public void toggle() {
		visible = !visible;
		if (visible) {
			Sounds.open.play(0.5f);
		} else {
			Sounds.close.play(0.5f);
		}
	}
}