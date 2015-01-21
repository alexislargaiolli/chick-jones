package fr.alex.games.screens;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.alex.games.GM;
import fr.alex.games.items.Item;
import fr.alex.games.items.ItemManager;
import fr.alex.games.localization.LocalManager;
import fr.alex.games.saves.PlayerManager;

public class ShopScreen extends MenuScreen {
	private Label playerGold;	

	public ShopScreen() {
		super();
	}

	@Override
	public void show() {
		playerGold.setText("Gold: " + PlayerManager.get().getGold());
		super.show();
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	protected void init() {
		mainTable.add("Item Shop", "title").expandX();
		mainTable.row();
		playerGold = new Label("Gold: " + PlayerManager.get().getGold(), GM.skin);
		mainTable.add(playerGold);
		mainTable.row();
		
		
		Table t = new Table(GM.skin);
		
		t.debug();
		t.pad(40).defaults().expandX().space(4);
		List<Item> items = ItemManager.get().getItems();
		List<Item> playerItems = ItemManager.get().getPlayerItem();
		for (final Item item : items) {
			
			Image icon = new Image(GM.skin.getRegion("Help"));
			t.add(icon).padTop(10);
			VerticalGroup v = new VerticalGroup();			
			v.align(Align.left);
			v.addActor(new Label(item.getName(), GM.skin));
			v.addActor(new Label(item.getDesc(), GM.skin, "small"));
			t.add(v).padTop(10).left();
			t.row().colspan(2);
			
			HorizontalGroup v2 = new HorizontalGroup();	
			v2.addActor(new Label("Prix: " + item.getGold(), GM.skin,  "small"));
			final Button bt = new Button(GM.skin, "buy");
			if(playerItems.contains(item)){
				bt.addAction(Actions.alpha(0.5f, 0));
			}
			else{
				bt.addListener(new ClickListener(){

					@Override
					public void clicked(InputEvent event, float x, float y) {
						if (ItemManager.get().buyItem(item)) {
							playerGold.setText("Gold: " + PlayerManager.get().getGold());
							
							String text = LocalManager.get().getHUDString("shop.selected.alreadybuy");							
							bt.addAction(Actions.alpha(0.5f, 1f));
						}
						super.clicked(event, x, y);
					}
					
				});
			}
			v2.addActor(bt);
			t.add(v2).right();
			
			t.row();
		}
				
		ScrollPane container = new ScrollPane(t, GM.skin);
		Container<ScrollPane> scrollContainer = new Container<ScrollPane>(container);
		scrollContainer.setBackground(GM.skin.getDrawable("panel"));
		scrollContainer.pad(45, 10, 30, 10);
		mainTable.add(scrollContainer);
		mainTable.debug();
	}

	@Override
	protected void onBack() {
		ScreenManager.getInstance().show(Screens.MAIN_MENU);
	}

}
