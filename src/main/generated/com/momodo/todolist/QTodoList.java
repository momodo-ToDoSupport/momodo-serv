package com.momodo.todolist;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTodoList is a Querydsl query type for TodoList
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTodoList extends EntityPathBase<TodoList> {

    private static final long serialVersionUID = -820677412L;

    public static final QTodoList todoList = new QTodoList("todoList");

    public final com.momodo.commons.QBaseEntity _super = new com.momodo.commons.QBaseEntity(this);

    public final NumberPath<Long> completedCount = createNumber("completedCount", Long.class);

    public final NumberPath<Long> count = createNumber("count", Long.class);

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final DatePath<java.time.LocalDate> dueDate = createDate("dueDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final NumberPath<Integer> step = createNumber("step", Integer.class);

    public QTodoList(String variable) {
        super(TodoList.class, forVariable(variable));
    }

    public QTodoList(Path<? extends TodoList> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTodoList(PathMetadata metadata) {
        super(TodoList.class, metadata);
    }

}

