import de.oglimmer.cyc.api.Food;

public class MenuEntryRuleImpl implements de.oglimmer.cyc.api.IMenuEntryRule {
	
	public org.slf4j.Logger log;
	
	public int getDeliciousness(Object ingredients, Object price) {		
		return ingredients.size();
	}
	
};