package de.oglimmer.cyc.api;

import java.util.Collection;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

/**
 * Runs multi-threaded.
 * 
 * @author oli
 *
 */
@Slf4j
public abstract class Guest {

	public abstract Set<Company> getAlreadyVisited();
	
	public abstract Long getAlreadyVisitedHash();

	public abstract void addAlreadyVisited(Company company);

	public void serve(CityProcessor cityProcessor) {
		incTotalGuestsDailyStats(cityProcessor);
		GuestDispatcher guestDisp = cityProcessor.getGuestDispMngr().getDispatcher(this);
		if (guestDisp.hasRestaurants()) {
			Establishment est = guestDisp.getRandom();
			log.debug("A guest decided for {} by {}", est.getAddress(), est.getParent().getName());
			addAlreadyVisited(est.getParent());
			walkIntoRestaurant(cityProcessor, est);
		} else {
			log.debug("No restaurant found for {}", guestDisp.getCity());
		}
	}

	private void incTotalGuestsDailyStats(CityProcessor cityProcessor) {
		for (Company company : cityProcessor.getGame().getCompanies()) {
			cityProcessor.getDailyStatisticsManager().getCollecting(company).getGuestsTotalPerCityMap()
					.add(cityProcessor.getCity().getName(), 1L);
		}
	}

	private void walkIntoRestaurant(CityProcessor cityProcessor, Establishment est) {
		Company company = est.getParent();
		Game game = company.getGame();
		game.getResult().getCreateNotExists(company.getName()).getGuestsYouPerCity().add(cityProcessor.getCity().getName(), 1);
		cityProcessor.getDailyStatisticsManager().getCollecting(company).getGuestsPerEstablishmentMap()
				.add(est.getAddress(), 1L);
		try {
			if (company.getMenu().size() > 0) {
				selectMenu(cityProcessor, est);
			}
		} catch (MissingIngredient e) {
			game.getResult().getCreateNotExists(company.getName()).addGuestsOutOfIngPerCity(cityProcessor.getCity().getName());
			game.getResult().getCreateNotExists(company.getName()).addMissingIngredients(e.getMissingIngredients());
			cityProcessor.getDailyStatisticsManager().getCollecting(company).getGuestsOutOfIngPerEstablishmentMap()
					.add(est.getAddress(), 1L);
			cityProcessor.getDailyStatisticsManager().getCollecting(company)
					.addMissingIngredientsPerFood(e.getMissingIngredients(), est);
			log.debug("Unable to prepare meal, missing {} in {}", e.getMissingIngredients(), est.getAddress());
		}
	}

	private void selectMenu(CityProcessor cityProcessor, Establishment est) throws MissingIngredient {
		Collection<MenuEntry> foodSelCol = selectMenuWrapper(est.getParent());
		if (foodSelCol.isEmpty()) {
			est.getParent().getGame().getResult().getCreateNotExists(est.getParent().getName()).getGuestsLeftPerCity()
					.add(cityProcessor.getCity().getName(), 1);
			cityProcessor.getDailyStatisticsManager().getCollecting(est.getParent())
					.getGuestsLeftPerEstablishmentMap().add(est.getAddress(), 1L);
			log.debug("Guest went to {} in {} and ordered nothing", est.getParent().getName(), est.getAddress());
		} else {
			for (MenuEntry menu : foodSelCol) {
				cityProcessor.serveDish(est, menu);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private Collection<MenuEntry> selectMenuWrapper(Company c) {
		return (Collection<MenuEntry>) GuestRule.INSTACE.selectMenu(c);
	}

}
