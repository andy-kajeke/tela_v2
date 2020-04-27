package com.planetsystems.tela.data.ClockIn;

import android.app.Application;

import com.planetsystems.tela.data.TelaRoomDatabase;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ClockInRepository {
    private static ClockInRepository INSTANCE;
    private SyncClockInDao syncClockInDao;

    private ClockInRepository(TelaRoomDatabase telaRoomDatabase) {
        syncClockInDao = telaRoomDatabase.getSyncClockInDao();
    }

    public static ClockInRepository getInstance(final TelaRoomDatabase telaRoomDatabase) {
        if (INSTANCE == null) {
            synchronized (ClockInRepository.class) {
                if ( INSTANCE == null ) {
                    INSTANCE = new ClockInRepository(telaRoomDatabase);
                }
            }
        }
        return INSTANCE;
    }

    public void synClockInTeacher(final SyncClockIn clockIn){
        TelaRoomDatabase.db_executor.execute(new Runnable() {
            @Override
            public void run() {
                syncClockInDao.syncClockInTeacherWithID(clockIn);
            }
        });
    }

    public List<SyncClockIn> getSyncClockInByEmployeeIDAndDay(final String employeeID, final String day) throws ExecutionException, InterruptedException {
        Callable<List<SyncClockIn>> collable = new Callable<List<SyncClockIn>>() {
            @Override
            public List<SyncClockIn> call() throws Exception {
                return syncClockInDao.getSyncClockInByEmployeeIDAndDay(employeeID, day);
            }
        };
        Future<List<SyncClockIn>> future = TelaRoomDatabase.db_executor.submit(collable);
        return future.get();
    }
}
