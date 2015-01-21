package fr.alex.games.screens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.alex.games.GM;
import fr.alex.games.items.Item;
import fr.alex.games.items.ItemManager;
import fr.alex.games.items.ItemType;
import fr.alex.games.localization.LocalManager;
import fr.alex.games.saves.PlayerManager;

public class PlayerScreen extends MenuScreen {

	private Map<ItemType, List<Item>> itemsByType;
	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void init() {
		mainTable.add("Equipements", "title").expandX().colspan(3).top();
		mainTable.row();
		itemsByType = new HashMap<ItemType, List<Item>>();

		List<Item> playerItems = ItemManager.get().getPlayerItem();
		for (Item item : playerItems) {
			List<Item> items = itemsByType.get(item.getType());
			if (items == null) {
				items = new ArrayList<Item>();
				itemsByType.put(item.getType(), items);
			}
			items.add(item);
		}

		for (ItemType type : ItemType.values()) {
			final Dialog dialog = new Dialog(type.name(), GM.skin);
			
			dialog.button(new Button(GM.skin, "bt-cancel"));
			dialog.setSize(VIRTUAL_WIDTH * .75f, VIRTUAL_HEIGHT * .5f);
			Image icon = new Image(GM.skin.getRegion("Help"));
			mainTable.add(icon).padTop(10);
			final Label itemLabel = new Label(type.name(), GM.skin);
			
			int id = PlayerManager.get().getEquipedItem(type);
			if (id != -1) {
				Item i = ItemManager.get().getItem(id);
				itemLabel.setText(i.getName());
			}
			mainTable.add(itemLabel).padTop(10);
			final TextButton bt = new TextButton(LocalManager.get().getHUDString("player.item.equipe"), GM.skin);
			bt.addListener(new ClickListener() {

				@Override
				public void clicked(InputEvent event, float x, float y) {
					dialog.show(stage);
					super.clicked(event, x, y);
				}

			});
			mainTable.add(bt).right();

			mainTable.row();

			List<Item> items = itemsByType.get(type);
			if (items == null) {
				items = new ArrayList<Item>();
				itemsByType.put(type, items);
			}

			Table t = new Table(GM.skin);
			for (final Item item : items) {
				icon = new Image(GM.skin.getRegion("Help"));
				t.add(icon).padTop(10);
				VerticalGroup v = new VerticalGroup();
				v.align(Align.left);
				v.addActor(new Label(item.getName(), GM.skin));
				v.addActor(new Label(item.getDesc(), GM.skin, "small"));
				t.add(v).left().padTop(10).padLeft(10);
				t.row().colspan(2);
				final TextButton btEquip = new TextButton("EQUIPER", GM.skin);
				btEquip.addListener(new ClickListener() {

					@Override
					public void clicked(InputEvent event, float x, float y) {
						if (btEquip.isChecked()) {
							PlayerManager.get().equipedItem(item);
							itemLabel.setText(item.getName());
							ItemManager.get().refreshList();
							dialog.hide();
						}
						super.clicked(event, x, y);
					}

				});
				t.add(btEquip).right();

				t.row();
			}
			ScrollPane container = new ScrollPane(t, GM.skin);
			dialog.getContentTable().add(container).pad(50, 50, 50, 50);
		}

		mainTable.debug();
	}

	@Override
	protected void onBack() {
		ScreenManager.getInstance().show(Screens.MAIN_MENU);
	}

}
