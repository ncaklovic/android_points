package com.crobridge.points

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.crobridge.points.db.Point
import com.crobridge.points.db.Polyline
import com.crobridge.points.db.PointDao
import com.crobridge.points.db.PointDb
import kotlinx.coroutines.runBlocking
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class PointDbTest {

    private lateinit var pointDao: PointDao
    private lateinit var db: PointDb

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, PointDb::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        pointDao = db.pointDbDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetPoint() = runBlocking {
        val p = Point()
        val n1 = pointDao.insert(p)
        val n2 = pointDao.insert(p)
        val points = pointDao.getAllPoints()
        assertEquals(2, points.value?.size)
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetPolyline() = runBlocking {
        val p = Polyline()
        p.name = "one"
        val n1 = pointDao.insert(p)
        p.name = "two"
        val n2 = pointDao.insert(p)
        assertEquals(2, n2)
        val polylines = pointDao.getAllPolylines()
        assertEquals(2, polylines.value?.size)
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.crobridge.points", appContext.packageName)
    }
}