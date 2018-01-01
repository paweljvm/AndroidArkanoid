package self.pingpong.view;

import self.pingpong.model.SquareCreator;




public class BonusSquare extends Square{
	public BonusSquare() {
		super();
		setBonus(true);
		int values = BonusType.values().length;
		bonusType =BonusType.values()[SquareCreator.randomizer.nextInt(values)];
	}

}
