package com.workspace.manager.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.EntityUpsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AssetDao_Impl implements AssetDao {
  private final RoomDatabase __db;

  private final SharedSQLiteStatement __preparedStmtOfMarkSynced;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAsset;

  private final SharedSQLiteStatement __preparedStmtOfUpdateRotation;

  private final SharedSQLiteStatement __preparedStmtOfUpdateSortOrder;

  private final EntityUpsertionAdapter<AssetEntity> __upsertionAdapterOfAssetEntity;

  public AssetDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__preparedStmtOfMarkSynced = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE assets SET isPendingSync = 0 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAsset = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM assets WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateRotation = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE assets SET rotationAngle = ?, updatedAt = ?, isPendingSync = 1 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateSortOrder = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE assets SET sortOrder = ? WHERE id = ?";
        return _query;
      }
    };
    this.__upsertionAdapterOfAssetEntity = new EntityUpsertionAdapter<AssetEntity>(new EntityInsertionAdapter<AssetEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT INTO `assets` (`id`,`downloadUrl`,`localUri`,`rotationAngle`,`sortOrder`,`createdAt`,`updatedAt`,`isPendingSync`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AssetEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getDownloadUrl());
        if (entity.getLocalUri() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getLocalUri());
        }
        statement.bindDouble(4, entity.getRotationAngle());
        statement.bindLong(5, entity.getSortOrder());
        statement.bindLong(6, entity.getCreatedAt());
        statement.bindLong(7, entity.getUpdatedAt());
        final int _tmp = entity.isPendingSync() ? 1 : 0;
        statement.bindLong(8, _tmp);
      }
    }, new EntityDeletionOrUpdateAdapter<AssetEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE `assets` SET `id` = ?,`downloadUrl` = ?,`localUri` = ?,`rotationAngle` = ?,`sortOrder` = ?,`createdAt` = ?,`updatedAt` = ?,`isPendingSync` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AssetEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getDownloadUrl());
        if (entity.getLocalUri() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getLocalUri());
        }
        statement.bindDouble(4, entity.getRotationAngle());
        statement.bindLong(5, entity.getSortOrder());
        statement.bindLong(6, entity.getCreatedAt());
        statement.bindLong(7, entity.getUpdatedAt());
        final int _tmp = entity.isPendingSync() ? 1 : 0;
        statement.bindLong(8, _tmp);
        statement.bindString(9, entity.getId());
      }
    });
  }

  @Override
  public Object upsertAssets(final List<AssetEntity> assets,
      final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> AssetDao.DefaultImpls.upsertAssets(AssetDao_Impl.this, assets, __cont), $completion);
  }

  @Override
  public Object markSynced(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkSynced.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMarkSynced.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAsset(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAsset.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAsset.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateRotation(final String id, final float angle, final long updatedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateRotation.acquire();
        int _argIndex = 1;
        _stmt.bindDouble(_argIndex, angle);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, updatedAt);
        _argIndex = 3;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateRotation.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSortOrder(final String id, final long sortOrder,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateSortOrder.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, sortOrder);
        _argIndex = 2;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateSortOrder.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertAsset(final AssetEntity asset, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfAssetEntity.upsert(asset);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<AssetEntity>> observeAllAssets() {
    final String _sql = "SELECT * FROM assets ORDER BY sortOrder ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"assets"}, new Callable<List<AssetEntity>>() {
      @Override
      @NonNull
      public List<AssetEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDownloadUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadUrl");
          final int _cursorIndexOfLocalUri = CursorUtil.getColumnIndexOrThrow(_cursor, "localUri");
          final int _cursorIndexOfRotationAngle = CursorUtil.getColumnIndexOrThrow(_cursor, "rotationAngle");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfIsPendingSync = CursorUtil.getColumnIndexOrThrow(_cursor, "isPendingSync");
          final List<AssetEntity> _result = new ArrayList<AssetEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AssetEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpDownloadUrl;
            _tmpDownloadUrl = _cursor.getString(_cursorIndexOfDownloadUrl);
            final String _tmpLocalUri;
            if (_cursor.isNull(_cursorIndexOfLocalUri)) {
              _tmpLocalUri = null;
            } else {
              _tmpLocalUri = _cursor.getString(_cursorIndexOfLocalUri);
            }
            final float _tmpRotationAngle;
            _tmpRotationAngle = _cursor.getFloat(_cursorIndexOfRotationAngle);
            final long _tmpSortOrder;
            _tmpSortOrder = _cursor.getLong(_cursorIndexOfSortOrder);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final boolean _tmpIsPendingSync;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPendingSync);
            _tmpIsPendingSync = _tmp != 0;
            _item = new AssetEntity(_tmpId,_tmpDownloadUrl,_tmpLocalUri,_tmpRotationAngle,_tmpSortOrder,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsPendingSync);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getPendingSyncAssets(final Continuation<? super List<AssetEntity>> $completion) {
    final String _sql = "SELECT * FROM assets WHERE isPendingSync = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<AssetEntity>>() {
      @Override
      @NonNull
      public List<AssetEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDownloadUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadUrl");
          final int _cursorIndexOfLocalUri = CursorUtil.getColumnIndexOrThrow(_cursor, "localUri");
          final int _cursorIndexOfRotationAngle = CursorUtil.getColumnIndexOrThrow(_cursor, "rotationAngle");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfIsPendingSync = CursorUtil.getColumnIndexOrThrow(_cursor, "isPendingSync");
          final List<AssetEntity> _result = new ArrayList<AssetEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AssetEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpDownloadUrl;
            _tmpDownloadUrl = _cursor.getString(_cursorIndexOfDownloadUrl);
            final String _tmpLocalUri;
            if (_cursor.isNull(_cursorIndexOfLocalUri)) {
              _tmpLocalUri = null;
            } else {
              _tmpLocalUri = _cursor.getString(_cursorIndexOfLocalUri);
            }
            final float _tmpRotationAngle;
            _tmpRotationAngle = _cursor.getFloat(_cursorIndexOfRotationAngle);
            final long _tmpSortOrder;
            _tmpSortOrder = _cursor.getLong(_cursorIndexOfSortOrder);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final boolean _tmpIsPendingSync;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPendingSync);
            _tmpIsPendingSync = _tmp != 0;
            _item = new AssetEntity(_tmpId,_tmpDownloadUrl,_tmpLocalUri,_tmpRotationAngle,_tmpSortOrder,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsPendingSync);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAssetById(final String id, final Continuation<? super AssetEntity> $completion) {
    final String _sql = "SELECT * FROM assets WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<AssetEntity>() {
      @Override
      @Nullable
      public AssetEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDownloadUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadUrl");
          final int _cursorIndexOfLocalUri = CursorUtil.getColumnIndexOrThrow(_cursor, "localUri");
          final int _cursorIndexOfRotationAngle = CursorUtil.getColumnIndexOrThrow(_cursor, "rotationAngle");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfIsPendingSync = CursorUtil.getColumnIndexOrThrow(_cursor, "isPendingSync");
          final AssetEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpDownloadUrl;
            _tmpDownloadUrl = _cursor.getString(_cursorIndexOfDownloadUrl);
            final String _tmpLocalUri;
            if (_cursor.isNull(_cursorIndexOfLocalUri)) {
              _tmpLocalUri = null;
            } else {
              _tmpLocalUri = _cursor.getString(_cursorIndexOfLocalUri);
            }
            final float _tmpRotationAngle;
            _tmpRotationAngle = _cursor.getFloat(_cursorIndexOfRotationAngle);
            final long _tmpSortOrder;
            _tmpSortOrder = _cursor.getLong(_cursorIndexOfSortOrder);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final boolean _tmpIsPendingSync;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPendingSync);
            _tmpIsPendingSync = _tmp != 0;
            _result = new AssetEntity(_tmpId,_tmpDownloadUrl,_tmpLocalUri,_tmpRotationAngle,_tmpSortOrder,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsPendingSync);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
