package com.momodo.emojihistory;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QEmojiHistory is a Querydsl query type for EmojiHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEmojiHistory extends EntityPathBase<EmojiHistory> {

    private static final long serialVersionUID = -347797028L;

    public static final QEmojiHistory emojiHistory = new QEmojiHistory("emojiHistory");

    public final com.momodo.commons.QBaseEntity _super = new com.momodo.commons.QBaseEntity(this);

    //inherited
    public final NumberPath<Long> createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath emoji = createString("emoji");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final NumberPath<Long> lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public QEmojiHistory(String variable) {
        super(EmojiHistory.class, forVariable(variable));
    }

    public QEmojiHistory(Path<? extends EmojiHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEmojiHistory(PathMetadata metadata) {
        super(EmojiHistory.class, metadata);
    }

}

