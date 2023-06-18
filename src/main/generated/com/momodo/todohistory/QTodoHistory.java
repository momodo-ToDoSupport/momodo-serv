package com.momodo.todohistory;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTodoHistory is a Querydsl query type for TodoHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTodoHistory extends EntityPathBase<TodoHistory> {

    private static final long serialVersionUID = -957402020L;

    public static final QTodoHistory todoHistory = new QTodoHistory("todoHistory");

    public final com.momodo.commons.QBaseEntity _super = new com.momodo.commons.QBaseEntity(this);

    public final NumberPath<Long> completedCount = createNumber("completedCount", Long.class);

    public final NumberPath<Long> count = createNumber("count", Long.class);

    //inherited
    public final NumberPath<Long> createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final DatePath<java.time.LocalDate> dueDate = createDate("dueDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final NumberPath<Long> lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final NumberPath<Integer> step = createNumber("step", Integer.class);

    public QTodoHistory(String variable) {
        super(TodoHistory.class, forVariable(variable));
    }

    public QTodoHistory(Path<? extends TodoHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTodoHistory(PathMetadata metadata) {
        super(TodoHistory.class, metadata);
    }

}

