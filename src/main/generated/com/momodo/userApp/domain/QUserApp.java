package com.momodo.userApp.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserApp is a Querydsl query type for UserApp
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserApp extends EntityPathBase<UserApp> {

    private static final long serialVersionUID = -1733487498L;

    public static final QUserApp userApp = new QUserApp("userApp");

    public final com.momodo.commons.QBaseTimeEntity _super = new com.momodo.commons.QBaseTimeEntity(this);

    public final StringPath accessToken = createString("accessToken");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath introduce = createString("introduce");

    public final BooleanPath isActive = createBoolean("isActive");

    public final BooleanPath isMarketing = createBoolean("isMarketing");

    public final BooleanPath isPush = createBoolean("isPush");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final StringPath phone = createString("phone");

    public final StringPath profileImage = createString("profileImage");

    public final StringPath refreshToken = createString("refreshToken");

    public final EnumPath<Role> roles = createEnum("roles", Role.class);

    public final StringPath tier = createString("tier");

    public final NumberPath<Long> tokenWeight = createNumber("tokenWeight", Long.class);

    public final EnumPath<UserType> type = createEnum("type", UserType.class);

    public final StringPath userId = createString("userId");

    public QUserApp(String variable) {
        super(UserApp.class, forVariable(variable));
    }

    public QUserApp(Path<? extends UserApp> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserApp(PathMetadata metadata) {
        super(UserApp.class, metadata);
    }

}

