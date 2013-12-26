package de.oglimmer.cyc.api;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import de.oglimmer.cyc.api.Constants.Mode;

public class FoodUnitTest {

	@Test
	public void testIncDay() {
		Game game = new Game(Mode.FULL);
		Company company = new Company(game, "companyA", game.getGrocer());
		Establishment est = new Establishment(company, "cityA", 5, 50, 1000, 2000);

		FoodUnit fu = new FoodUnit(Food.BEEF_MEAT, 10);
		for (int i = 0; i < 9; i++) {
			Assert.assertEquals(10 - i, fu.getPullDate());
			Assert.assertFalse(fu.incDay(est));
			Assert.assertEquals(9 - i, fu.getPullDate());
			Assert.assertEquals(10, fu.getUnits());
		}
		Assert.assertTrue(fu.incDay(est));
		Assert.assertEquals(0, fu.getUnits());
		Assert.assertEquals(0, fu.getPullDate());
	}

	@Test
	public void testSatisfyIngredient() {
		FoodUnit fu = new FoodUnit(Food.BEEF_MEAT, 2);
		Set<FoodUnit> set = new HashSet<>();
		set.add(fu);
		Assert.assertTrue(FoodUnit.satisfyIngredient(set, Food.BEEF_MEAT));
		Assert.assertEquals(1, fu.getUnits());
		Assert.assertTrue(FoodUnit.satisfyIngredient(set, Food.BEEF_MEAT));
		Assert.assertEquals(0, fu.getUnits());
		Assert.assertFalse(FoodUnit.satisfyIngredient(set, Food.BEEF_MEAT));
	}
}
