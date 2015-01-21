package fr.alex.games.widget;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import fr.alex.games.items.Item;

public class CustomListItem extends Table {
	private Item item;
	private Image icon;
	private Label lbName;

	public CustomListItem(Item item, Skin skin) {
		super();
		icon = new Image(skin.getRegion("Help"));
		this.add(icon).width(64).height(64);
		this.item = item;
		lbName = new Label(item.getName(), skin);
		this.add(lbName).expandX();
	}
	
	public void setSelected(){
		lbName.setFontScale(1.1f);
	}
	
	public void unSelect(){
		lbName.setFontScale(1f);
	}

	public Item getItem() {
		return item;
	}

}
