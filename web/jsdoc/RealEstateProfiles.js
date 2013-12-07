
/**
* Container object used as the parameter for the Campaign method realEstateAgent.
* Container class (behaves like a <a href="List.html">List</a>).<br/>
* <br/>
* The parameter in the company.realEstateAgent function. 
* Before looping through the objects of <a href="RealEstateProfile.html">RealEstateProfile</a>
* you probably want to use one of the sortBy* methods in this class.<br/>
* If many players are in the game there will more than one city. In this case you can loop through the
* citiesToRestaurants property to see how many restaurants are in each city. Keep in mind that a city
* has only a certain number of guests, thus the more restaurants are in a city 
* the more competitive struggle there is.
*
* @class RealEstateProfiles
* @constructor
*/
function RealEstateProfiles() {
}

/**
 * A <a href="List.html">list</a> of <a href="DataPair.html">DataPair</a> with name=name of a city and value=total number of restaurants in this city. 
 * 
 * @property citiesToRestaurants
 * @type {List}
 */
RealEstateProfiles.prototype.citiesToRestaurants;

/**
* Java-like iterator to loop over all RealEstateProfiles within this presentation.
*
* @method iterator
* @return {Iterator} An iterator to loop
*/
RealEstateProfiles.prototype.iterator = function() {
};

/**
* Returns a RealEstateProfile object at the index
* 
*
* @method get
* @param {Number} index index number to get
* @return {RealEstateProfile} the element at the position index
*/
RealEstateProfiles.prototype.get = function(index) {	
};

/**
* Sorts the list by lease cost with ascending order and returns the RealEstateProfiles object itself
* 
*
* @method sortByLeaseCost
* @return {RealEstateProfiles} returns self
*/
RealEstateProfiles.prototype.sortByLeaseCost = function() {	
};

/**
* Sorts the list by sales price with ascending order and returns the RealEstateProfiles object itself
* 
*
* @method sortBySalePrice
* @return {RealEstateProfiles} returns self
*/
RealEstateProfiles.prototype.sortBySalePrice = function() {	
};

/**
* Sorts the list by location quality with ascending order and returns the RealEstateProfiles object itself
* 
*
* @method sortByLocationQuality
* @return {RealEstateProfiles} returns self
*/
RealEstateProfiles.prototype.sortByLocationQuality = function() {	
};

/**
* Sorts the list by location size with ascending order and returns the RealEstateProfiles object itself
* 
*
* @method sortByLocationSize
* @return {RealEstateProfiles} returns self
*/
RealEstateProfiles.prototype.sortByLocationSize = function() {	
};

/**
* Returns the list size
*
* @method size
* @return {Number} the list size
*/
RealEstateProfiles.prototype.size = function() {	
};

/**
* JavaScript-like callback to loop over all RealEstateProfile within this call.
* 
*
* @method each
* @param {Function} forEachElement Will be called for each available RealEstateProfile. Needs to have one parameter.
*/
RealEstateProfiles.prototype.each = function() {	
};
