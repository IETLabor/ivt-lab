package hu.bme.mit.spaceship;

/**
* A simple spaceship with two proton torpedo stores and four lasers
*/
public class GT4500 implements SpaceShip {

  private TorpedoStore primaryTorpedoStore;
  private TorpedoStore secondaryTorpedoStore;

  private boolean wasPrimaryFiredLast = false;

  private enum FiringResult {
    PRIMARY,
    SECONDARY,
    EMPTY
  }

  public GT4500() {
    this.primaryTorpedoStore = new TorpedoStore(10);
    this.secondaryTorpedoStore = new TorpedoStore(10);
  }

  public boolean fireLaser(FiringMode firingMode) {
    // TODO not implemented yet
    return false;
  }

  /**
  * Tries to fire the torpedo stores of the ship.
  *
  * @param firingMode how many torpedo bays to fire
  * 	SINGLE: fires only one of the bays.
  * 			- For the first time the primary store is fired.
  * 			- To give some cooling time to the torpedo stores, torpedo stores are fired alternating.
  * 			- But if the store next in line is empty, the ship tries to fire the other store.
  * 			- If the fired store reports a failure, the ship does not try to fire the other one.
  * 	ALL:	tries to fire both of the torpedo stores.
  *
  * @return whether at least one torpedo was fired successfully
  */
  @Override
  public boolean fireTorpedo(FiringMode firingMode) {
    if (firingMode == FiringMode.SINGLE) {
      return fireSingle();
    } else if(firingMode == FiringMode.ALL) {
      return fireMultiple();
    }
    return false;
  }

  private FiringResult fireSingleWithFallback(TorpedoStore primary, TorpedoStore fallback,
                                              FiringResult primaryResult, FiringResult fallbackResult) {
    if (!primary.isEmpty()) {
      return primary.fire(1) ? primaryResult : FiringResult.EMPTY;
    } else if (!fallback.isEmpty()) {
      return fallback.fire(1) ? fallbackResult : FiringResult.EMPTY;
    }
    return FiringResult.EMPTY;
  }

  private boolean fireSingle() {
    FiringResult result;
    if (wasPrimaryFiredLast) {
      result = fireSingleWithFallback(secondaryTorpedoStore, primaryTorpedoStore,
                                      FiringResult.SECONDARY, FiringResult.PRIMARY);
    } else {
      result = fireSingleWithFallback(primaryTorpedoStore, secondaryTorpedoStore,
                                      FiringResult.PRIMARY, FiringResult.SECONDARY);
    }
    wasPrimaryFiredLast = result == FiringResult.PRIMARY;
    return result != FiringResult.EMPTY;
  }

  private boolean fireMultiple() {
    // try to fire both of the torpedo stores
    if (!primaryTorpedoStore.isEmpty()) {
      primaryTorpedoStore.fire(1);
      return true;
    }
    if (!secondaryTorpedoStore.isEmpty()) {
      secondaryTorpedoStore.fire(1);
      return true;
    }
    return false;
  }
}
