package com.planetsystems.tela.data.clockOut;

import com.planetsystems.tela.data.TelaRoomDatabase;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ClockOutRepository {
    private static volatile ClockOutRepository INSTANCE;
    private SyncClockOutDao syncClockOutDao;

    private ClockOutRepository(TelaRoomDatabase telaRoomDatabase) {
        syncClockOutDao = telaRoomDatabase.getSyncClockOutDao();
    }

    public static ClockOutRepository getInstance(TelaRoomDatabase telaRoomDatabase) {
        if (INSTANCE == null ) {
            synchronized (ClockOutRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ClockOutRepository(telaRoomDatabase);
                }
            }
        }
        return INSTANCE;
    }

    public void insertSynClockOut(final SyncClockOut syncClockOut) {
        TelaRoomDatabase.db_executor.execute(new Runnable() {
            @Override
            public void run() {
                syncClockOutDao.insertClockOutTeacher(syncClockOut);
            }
        });
    }

    public List<SyncClockOut> getSyncClockOutByEmployeeNumberAndDate(final String employeeNumber, final String date) throws ExecutionException, InterruptedException {
        Callable<List<SyncClockOut>> callable = new Callable<List<SyncClockOut>>() {
            @Override
            public List<SyncClockOut> call() throws Exception {
                return syncClockOutDao.getSyncClockOutByEmployeeIdAndDate(employeeNumber, date);
            }
        };
        Future<List<SyncClockOut>> future = TelaRoomDatabase.db_executor.submit(callable);
        return  future.get();
    }
}