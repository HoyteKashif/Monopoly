## Resources
* https://bytes.com/topic/java/insights/870013-text-based-menus
* https://bytes.com/topic/java/answers/869742-display-menu
* https://stackoverflow.com/questions/30249324/how-to-get-java-to-wait-for-user-input/30249614
* https://bugs.eclipse.org/bugs/show_bug.cgi?id=130932

## Checkout
* https://bytes.com/topic/java/insights

## FIXME
- [ ] Player purchasing already owned property is not handled properly
- [ ] Player wealth is Calculated without the collective price of the buildings owned by this player

## TODO
1. Implement Community Chest card selection

3. Check for any additional rules when the user roles doubles

4. Make it so that if the player throws doubles three times in succession, they go immediately to JAIL

6. Determine the order of the players by randomly rolling the dice then 
putting the players in order from highest to lowest value

7. If a player lands on a property and they do not wish to purchase
it allow their to be an auction. Bank sells the property to the highest bidder. Bidding may start at any price.

## IMPROVEMENTS
- [ ] Change the structure of the Chance class to follow the Singleton Pattern
- [ ] Remove any usage of System.out.print that is outside of the Game class