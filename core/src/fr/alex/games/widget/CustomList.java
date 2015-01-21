package fr.alex.games.widget;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.alex.games.items.Item;

public class CustomList {

	private ScrollPane pan;
	private Table mainTable;
	private CustomListItem selected;
	private Skin skin;
	private ItemSelectedEvent selectionEvent;

	public CustomList(Skin skin) {
		this.skin = skin;
		mainTable = new Table(skin);
		mainTable.debug();
		pan = new ScrollPane(mainTable, skin);
		
	}

	public void setItems(List<Item> items) {
		mainTable.clear();
		for (Item item : items) {
			final CustomListItem cli = new CustomListItem(item, skin);
			mainTable.add(cli).expandX().width(400);
			mainTable.row();
			cli.addListener(new ClickListener() {

				@Override
				public void clicked(InputEvent event, float x, float y) {
					if (selected != null) {
						selected.unSelect();
					}
					selected = cli;
					selected.setSelected();
					selectionEvent.onSelected(selected);
					super.clicked(event, x, y);
				}

			});
		}
	}

	public CustomListItem getSelected() {
		return selected;
	}

	public void setSelected(CustomListItem selected) {
		this.selected = selected;
	}

	public ItemSelectedEvent getSelectionEvent() {
		return selectionEvent;
	}

	public void setSelectionEvent(ItemSelectedEvent selectionEvent) {
		this.selectionEvent = selectionEvent;
	}

	public ScrollPane getPan() {
		return pan;
	}

	public void setPan(ScrollPane pan) {
		this.pan = pan;
	}
}
