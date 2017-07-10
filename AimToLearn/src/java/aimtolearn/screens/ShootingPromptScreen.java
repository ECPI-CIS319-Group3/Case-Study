package aimtolearn.screens;

import aimtolearn.Constants;
import aimtolearn.Game;
import aimtolearn.Utils;

import javax.swing.SwingConstants;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

public class ShootingPromptScreen extends MainScreen {

	private String prompt;
	private String[] optionStrings;
	private boolean active, initiated;
	private int[] disabledIndexes;
	private IntConsumer onSelection;

	private Rectangle[] options;

	private Rectangle promptBounds;

	private static final int PROMPT_HEIGHT = 200;
	private static final float FONT_SIZE = 40;

	public ShootingPromptScreen(Game game) {
		super(game);

		this.prompt = "";
		this.optionStrings = null;
		this.active = false;
		this.initiated = false;
		this.disabledIndexes = new int[0];
		this.onSelection = null;

		this.promptBounds = new Rectangle(0, 0, Constants.MAIN_WIDTH, PROMPT_HEIGHT);
	}

	public void setup(String prompt, String[] optionStrings, int[] disabledIndexes, IntConsumer onSelection) {
		this.prompt = prompt;
		this.optionStrings = optionStrings;
		this.options = new Rectangle[optionStrings.length];
		this.initiated = false;
		this.disabledIndexes = disabledIndexes == null ? new int[0] : disabledIndexes;
		this.onSelection = onSelection;

		this.active = true;
		startLoop();
	}

	public void deactivate() {
		this.active = false;
		stopLoop();
	}

	@Override
	protected void updateScreen(Graphics g) {

		if (!active) return;

		super.updateScreen(g);

		g.setFont(g.getFont().deriveFont(FONT_SIZE));

		if (!initiated) {
			FontMetrics metrics = g.getFontMetrics();
			List<Integer> optionWidths = new ArrayList<>();

			int optionsWidth = 0;
			int height = metrics.getHeight();

			for (String option : optionStrings) {
				int w = (int) metrics.getStringBounds(option, g).getWidth();
				optionsWidth += w;
				optionWidths.add(w);
			}

			int spacing = (Constants.MAIN_WIDTH - optionsWidth) / (optionStrings.length+1);
			int lastX = 0;

			for (int i = 0; i < options.length; i++) {
				Rectangle bounds = new Rectangle(lastX + spacing, PROMPT_HEIGHT, optionWidths.get(i), height);
				this.options[i] = bounds;
				lastX = (int) bounds.getMaxX();
			}

			this.initiated = true;
		}

		Utils.text(prompt, promptBounds, g, SwingConstants.CENTER);

		for (int i = 0; i < options.length; i++) {
			((Graphics2D) g).draw(options[i]);
			Utils.text(optionStrings[i], options[i], g, SwingConstants.CENTER);
		}

	}

	@Override
	protected void tick() {
		super.tick();

		for (int i = 0; i < options.length; i++) {

			for (Rectangle shot : shots) {
				if (shot.intersects(options[i])) {
					shots.remove(shot);
					onSelection.accept(i);
					deactivate();
				}
			}

		}

		repaint();
	}
}
