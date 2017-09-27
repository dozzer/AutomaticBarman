package com.saienko.androidthings.barman.db;

import android.support.annotation.WorkerThread;
import com.saienko.androidthings.barman.db.cocktail.Cocktail;
import com.saienko.androidthings.barman.db.cocktail.CocktailElement;
import com.saienko.androidthings.barman.db.cocktail.Component;
import com.saienko.androidthings.barman.db.drinkGroup.CocktailGroup;
import com.saienko.androidthings.barman.db.gpio.Gpio;
import com.saienko.androidthings.barman.db.motor.Motor;
import com.saienko.androidthings.barman.db.position.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/13/17
 * Time: 01:23
 */

public class DatabaseUtil {

    private DatabaseUtil() {
        throw new IllegalStateException("Utility class");
    }

    @WorkerThread
    private static List<Cocktail> load(List<Cocktail> cocktails) {
        for (Cocktail cocktail : cocktails) {
            load(cocktail);
        }
        return cocktails;
    }

    @WorkerThread
    private static Cocktail load(Cocktail cocktail) {
        List<CocktailElement> cocktailElements =
                AppDatabase.getDb().cocktailElementDao().getByCocktailId(cocktail.getCocktailId());

        for (CocktailElement cocktailElement : cocktailElements) {
            load(cocktailElement);
        }

        cocktail.setCocktailElements(cocktailElements);

        cocktail.setCocktailGroup(AppDatabase.getDb().cocktailGroup().getById(cocktail.getCocktailGroupId()));
        return cocktail;
    }

    @WorkerThread
    private static CocktailElement load(CocktailElement cocktailElement) {
        cocktailElement.setComponent(AppDatabase.getDb().componentDao().getById(cocktailElement.getComponentId()));
        Position position = AppDatabase.getDb().positionDao().getByComponentId(cocktailElement.getComponentId());
        if (position != null) {
            cocktailElement.setPosition(load(position));
        }
        return cocktailElement;
    }


    @WorkerThread
    public static List<Motor> getMotors() {
        List<Motor> motors = AppDatabase.getDb().motorDao().getAll();
        for (Motor motor : motors) {
            load(motor);
        }
        return motors;
    }

    @WorkerThread
    private static Motor load(Motor motor) {
        motor.setGpio(AppDatabase.getDb().gpioDao().get(motor.getGpioId()));
        return motor;
    }

    @WorkerThread
    public static List<Cocktail> getCocktails() {
        List<Cocktail> cocktailList = load(AppDatabase.getDb().cocktailDao().getAll());

        return getPossibleCocktails(cocktailList);
    }

    private static List<Cocktail> getPossibleCocktails(List<Cocktail> cocktailList) {
        List<Cocktail> out = new ArrayList<>(cocktailList.size());
        for (Cocktail cocktail : cocktailList) {
            if (isPositionCorrect(cocktail)) {
                out.add(cocktail);
            }
        }
        return out;
    }

    private static boolean isPositionCorrect(Cocktail cocktail) {
        for (CocktailElement cocktailElement : cocktail.getCocktailElements()) {
            if (cocktailElement.getPosition() == null || cocktailElement.getComponent() == null) {
                return false;
            }
        }
        return true;
    }

    @WorkerThread
    private static Position load(Position position) {
        Motor motor = AppDatabase.getDb().motorDao().getById(position.getMotorId());
        if (motor != null) {
            position.setMotor(load(motor));
        }
        position.setComponent(AppDatabase.getDb().componentDao().getById(position.getComponentId()));
        return position;
    }

    @WorkerThread
    public static List<Gpio> getFreeGpio() {
        List<Motor> usedMotors = AppDatabase.getDb().motorDao().getAll();
        long[]      gpioIds    = new long[usedMotors.size()];
        for (int i = 0; i < usedMotors.size(); i++) {
            gpioIds[i] = usedMotors.get(i).getGpioId();
        }
        return AppDatabase.getDb().gpioDao().getAllFree(gpioIds);
    }

    @WorkerThread
    public static List<Component> getComponents() {
        return AppDatabase.getDb().componentDao().getAll();
    }

    @WorkerThread
    public static List<Component> getFreeComponents() {
        List<Position>  positions    = DatabaseUtil.getPositions();
        ArrayList<Long> componentIds = new ArrayList<>();
        for (Position position : positions) {
            if (position.getComponentId() != -1) {
                componentIds.add(position.getComponentId());
            }
        }
        Long[] componentIdsArray = new Long[componentIds.size()];
        componentIds.toArray(componentIdsArray);

        return AppDatabase.getDb().componentDao().getAllFree(toPrimitives(componentIdsArray));
    }

    @WorkerThread
    public static List<Component> getFreeComponents(long[] componentIds) {
        return AppDatabase.getDb().componentDao().getAllFree(componentIds);
    }

    @WorkerThread
    public static List<Position> getPositions() {

        List<Motor>    motors        = getMotors();
        List<Motor>    unknownMotors = new ArrayList<>();
        List<Position> positions     = AppDatabase.getDb().positionDao().getAll();
        for (Position position : positions) {
            load(position);
        }


        for (Motor motor : motors) {
            boolean shouldBeAdded = true;
            for (Position position : positions) {
                if (position.getMotorId() == motor.getMotorId()) {
                    shouldBeAdded = false;
                    break;
                }
            }
            if (shouldBeAdded) {
                unknownMotors.add(motor);
            }
        }
        for (Motor unknownMotor : unknownMotors) {
            Position position = new Position(-1, unknownMotor.getMotorId());
            position.setMotor(unknownMotor);
            positions.add(position);
        }

        return positions;
    }

    @WorkerThread
    public static List<Gpio> getGpios() {
        return AppDatabase.getDb().gpioDao().getAll();
    }

    @WorkerThread
    public static List<CocktailGroup> getCocktailGroups() {
        return AppDatabase.getDb().cocktailGroup().getAll();
    }

    @WorkerThread
    public static void save(Cocktail cocktail) {
        long cocktailId = AppDatabase.getDb().cocktailDao().insert(cocktail);
        if (cocktail.getCocktailElements() != null) {
            DatabaseUtil.save(cocktailId, cocktail.getCocktailElements());
        }
    }

    @WorkerThread
    private static void save(long cocktailId, List<CocktailElement> cocktailElements) {
        if (cocktailElements != null) {
            for (CocktailElement element : cocktailElements) {
                element.setCocktailId(cocktailId);
            }
            CocktailElement[] array = new CocktailElement[cocktailElements.size()];
            cocktailElements.toArray(array);
            AppDatabase.getDb().cocktailElementDao().insertAll(array);
        }
    }

    @WorkerThread
    public static void update(Cocktail cocktail) {
        AppDatabase.getDb().cocktailDao().update(cocktail);
        deleteCocktailElements(cocktail.getCocktailId());
        save(cocktail.getCocktailId(), cocktail.getCocktailElements());
    }

    private static void deleteCocktailElements(long cocktailId) {
        AppDatabase.getDb().cocktailElementDao().delete(cocktailId);
    }

    public static void delete(Cocktail cocktail) {
        AppDatabase.getDb().cocktailDao().delete(cocktail.getCocktailId());
        for (CocktailElement cocktailElement : cocktail.getCocktailElements()) {
            AppDatabase.getDb().cocktailElementDao().delete(cocktailElement.getCocktailId());
        }
    }

    private static long[] toPrimitives(Long... objects) {

        long[] primitives = new long[objects.length];
        for (int i = 0; i < objects.length; i++)
            primitives[i] = objects[i];

        return primitives;
    }
}
