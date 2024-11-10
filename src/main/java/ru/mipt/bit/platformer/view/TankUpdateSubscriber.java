package ru.mipt.bit.platformer.view;

import ru.mipt.bit.platformer.model.TankModel;

public interface TankUpdateSubscriber {

    void onTankDestroyed(TankModel tankModel);

}
