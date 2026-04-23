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
public final class NoteDao_Impl implements NoteDao {
  private final RoomDatabase __db;

  private final SharedSQLiteStatement __preparedStmtOfMarkSynced;

  private final SharedSQLiteStatement __preparedStmtOfClearConflict;

  private final SharedSQLiteStatement __preparedStmtOfDeleteNote;

  private final SharedSQLiteStatement __preparedStmtOfUpdateSortOrder;

  private final EntityUpsertionAdapter<NoteEntity> __upsertionAdapterOfNoteEntity;

  public NoteDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__preparedStmtOfMarkSynced = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE notes SET isPendingSync = 0, isConflicted = 0 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearConflict = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE notes SET isConflicted = 0 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteNote = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM notes WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateSortOrder = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE notes SET sortOrder = ? WHERE id = ?";
        return _query;
      }
    };
    this.__upsertionAdapterOfNoteEntity = new EntityUpsertionAdapter<NoteEntity>(new EntityInsertionAdapter<NoteEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT INTO `notes` (`id`,`title`,`content`,`sortOrder`,`createdAt`,`updatedAt`,`isPendingSync`,`isConflicted`,`remoteContent`,`remoteUpdatedAt`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final NoteEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getContent());
        statement.bindLong(4, entity.getSortOrder());
        statement.bindLong(5, entity.getCreatedAt());
        statement.bindLong(6, entity.getUpdatedAt());
        final int _tmp = entity.isPendingSync() ? 1 : 0;
        statement.bindLong(7, _tmp);
        final int _tmp_1 = entity.isConflicted() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        if (entity.getRemoteContent() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getRemoteContent());
        }
        statement.bindLong(10, entity.getRemoteUpdatedAt());
      }
    }, new EntityDeletionOrUpdateAdapter<NoteEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE `notes` SET `id` = ?,`title` = ?,`content` = ?,`sortOrder` = ?,`createdAt` = ?,`updatedAt` = ?,`isPendingSync` = ?,`isConflicted` = ?,`remoteContent` = ?,`remoteUpdatedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final NoteEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getContent());
        statement.bindLong(4, entity.getSortOrder());
        statement.bindLong(5, entity.getCreatedAt());
        statement.bindLong(6, entity.getUpdatedAt());
        final int _tmp = entity.isPendingSync() ? 1 : 0;
        statement.bindLong(7, _tmp);
        final int _tmp_1 = entity.isConflicted() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        if (entity.getRemoteContent() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getRemoteContent());
        }
        statement.bindLong(10, entity.getRemoteUpdatedAt());
        statement.bindString(11, entity.getId());
      }
    });
  }

  @Override
  public Object upsertNotes(final List<NoteEntity> notes,
      final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> NoteDao.DefaultImpls.upsertNotes(NoteDao_Impl.this, notes, __cont), $completion);
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
  public Object clearConflict(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearConflict.acquire();
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
          __preparedStmtOfClearConflict.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteNote(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteNote.acquire();
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
          __preparedStmtOfDeleteNote.release(_stmt);
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
  public Object upsertNote(final NoteEntity note, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfNoteEntity.upsert(note);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<NoteEntity>> observeAllNotes() {
    final String _sql = "SELECT * FROM notes ORDER BY sortOrder ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"notes"}, new Callable<List<NoteEntity>>() {
      @Override
      @NonNull
      public List<NoteEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfIsPendingSync = CursorUtil.getColumnIndexOrThrow(_cursor, "isPendingSync");
          final int _cursorIndexOfIsConflicted = CursorUtil.getColumnIndexOrThrow(_cursor, "isConflicted");
          final int _cursorIndexOfRemoteContent = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteContent");
          final int _cursorIndexOfRemoteUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteUpdatedAt");
          final List<NoteEntity> _result = new ArrayList<NoteEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final NoteEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
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
            final boolean _tmpIsConflicted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsConflicted);
            _tmpIsConflicted = _tmp_1 != 0;
            final String _tmpRemoteContent;
            if (_cursor.isNull(_cursorIndexOfRemoteContent)) {
              _tmpRemoteContent = null;
            } else {
              _tmpRemoteContent = _cursor.getString(_cursorIndexOfRemoteContent);
            }
            final long _tmpRemoteUpdatedAt;
            _tmpRemoteUpdatedAt = _cursor.getLong(_cursorIndexOfRemoteUpdatedAt);
            _item = new NoteEntity(_tmpId,_tmpTitle,_tmpContent,_tmpSortOrder,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsPendingSync,_tmpIsConflicted,_tmpRemoteContent,_tmpRemoteUpdatedAt);
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
  public Flow<List<NoteEntity>> observeConflictedNotes() {
    final String _sql = "SELECT * FROM notes WHERE isConflicted = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"notes"}, new Callable<List<NoteEntity>>() {
      @Override
      @NonNull
      public List<NoteEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfIsPendingSync = CursorUtil.getColumnIndexOrThrow(_cursor, "isPendingSync");
          final int _cursorIndexOfIsConflicted = CursorUtil.getColumnIndexOrThrow(_cursor, "isConflicted");
          final int _cursorIndexOfRemoteContent = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteContent");
          final int _cursorIndexOfRemoteUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteUpdatedAt");
          final List<NoteEntity> _result = new ArrayList<NoteEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final NoteEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
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
            final boolean _tmpIsConflicted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsConflicted);
            _tmpIsConflicted = _tmp_1 != 0;
            final String _tmpRemoteContent;
            if (_cursor.isNull(_cursorIndexOfRemoteContent)) {
              _tmpRemoteContent = null;
            } else {
              _tmpRemoteContent = _cursor.getString(_cursorIndexOfRemoteContent);
            }
            final long _tmpRemoteUpdatedAt;
            _tmpRemoteUpdatedAt = _cursor.getLong(_cursorIndexOfRemoteUpdatedAt);
            _item = new NoteEntity(_tmpId,_tmpTitle,_tmpContent,_tmpSortOrder,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsPendingSync,_tmpIsConflicted,_tmpRemoteContent,_tmpRemoteUpdatedAt);
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
  public Object getPendingSyncNotes(final Continuation<? super List<NoteEntity>> $completion) {
    final String _sql = "SELECT * FROM notes WHERE isPendingSync = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<NoteEntity>>() {
      @Override
      @NonNull
      public List<NoteEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfIsPendingSync = CursorUtil.getColumnIndexOrThrow(_cursor, "isPendingSync");
          final int _cursorIndexOfIsConflicted = CursorUtil.getColumnIndexOrThrow(_cursor, "isConflicted");
          final int _cursorIndexOfRemoteContent = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteContent");
          final int _cursorIndexOfRemoteUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteUpdatedAt");
          final List<NoteEntity> _result = new ArrayList<NoteEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final NoteEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
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
            final boolean _tmpIsConflicted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsConflicted);
            _tmpIsConflicted = _tmp_1 != 0;
            final String _tmpRemoteContent;
            if (_cursor.isNull(_cursorIndexOfRemoteContent)) {
              _tmpRemoteContent = null;
            } else {
              _tmpRemoteContent = _cursor.getString(_cursorIndexOfRemoteContent);
            }
            final long _tmpRemoteUpdatedAt;
            _tmpRemoteUpdatedAt = _cursor.getLong(_cursorIndexOfRemoteUpdatedAt);
            _item = new NoteEntity(_tmpId,_tmpTitle,_tmpContent,_tmpSortOrder,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsPendingSync,_tmpIsConflicted,_tmpRemoteContent,_tmpRemoteUpdatedAt);
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
  public Object getNoteById(final String id, final Continuation<? super NoteEntity> $completion) {
    final String _sql = "SELECT * FROM notes WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<NoteEntity>() {
      @Override
      @Nullable
      public NoteEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfIsPendingSync = CursorUtil.getColumnIndexOrThrow(_cursor, "isPendingSync");
          final int _cursorIndexOfIsConflicted = CursorUtil.getColumnIndexOrThrow(_cursor, "isConflicted");
          final int _cursorIndexOfRemoteContent = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteContent");
          final int _cursorIndexOfRemoteUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "remoteUpdatedAt");
          final NoteEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
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
            final boolean _tmpIsConflicted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsConflicted);
            _tmpIsConflicted = _tmp_1 != 0;
            final String _tmpRemoteContent;
            if (_cursor.isNull(_cursorIndexOfRemoteContent)) {
              _tmpRemoteContent = null;
            } else {
              _tmpRemoteContent = _cursor.getString(_cursorIndexOfRemoteContent);
            }
            final long _tmpRemoteUpdatedAt;
            _tmpRemoteUpdatedAt = _cursor.getLong(_cursorIndexOfRemoteUpdatedAt);
            _result = new NoteEntity(_tmpId,_tmpTitle,_tmpContent,_tmpSortOrder,_tmpCreatedAt,_tmpUpdatedAt,_tmpIsPendingSync,_tmpIsConflicted,_tmpRemoteContent,_tmpRemoteUpdatedAt);
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
