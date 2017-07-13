package aimtolearn.screens;

import aimtolearn.Game;

public class ConfirmReturnMenu extends BaseMenu {

	private BaseScreen returnScreen;

	public ConfirmReturnMenu(Game game) {
		super(game, new String[]{"Yes", "No"}, "Are you sure you\nwant to return to\nthe main menu?");
		setTopHeight(500);
	}

	public void setReturnScreen(BaseScreen returnScreen) {
		this.returnScreen = returnScreen;
		reset();
	}

	@Override
	public void onSelection(int index) {
		if (index == 0) { // yes
			game.setDisplayPanel(game.MAIN_MENU);
		}
		else if (index == 1 && returnScreen != null) { // no
			game.setDisplayPanel(returnScreen);
			returnScreen.setActive(true);
		}
	}
}
