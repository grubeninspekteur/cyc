

/**
* Object used as the parameter for the Campaign method foodDelivery.
* Container class (behaves like a <a href="List.html">List</a>).<br/>
* <br/>
* Unless you own two ore more restaurants you don't need to deal with FoodDelivery other than in the
* example code.
*
* @class FoodDelivery
* @constructor
*/
function FoodDelivery() {
	
}

/**
* Java-like iterator to loop over all <a href="FoodUnit.html">FoodUnit</a> within this delivery.
*
* @method iterator
* @return {Iterator} An iterator to loop
*/
FoodDelivery.prototype.iterator = function() {
};

/**
* JavaScript-like callback to loop over all <a href="FoodUnit.html">FoodUnit</a> within this delivery.
* 
*
* @method each
* @param {Function} forEachElement Will be called for each available <a href="FoodUnit.html">FoodUnit</a>. Needs to have one parameter which will be a <a href="FoodUnit.html">FoodUnit</a>.
*/
FoodDelivery.prototype.each = function(forEachElement) {
};

/**
* Returns the container size
*
* @method size
* @return {Number} the container size
*/
FoodDelivery.prototype.size = function() {	
};

/**
* Returns a FoodUnit object at the index
* 
*
* @method get
* @param {Number} index index number to get
* @return {FoodUnit} the element at the position index
*/
FoodDelivery.prototype.get = function(index) {	
};