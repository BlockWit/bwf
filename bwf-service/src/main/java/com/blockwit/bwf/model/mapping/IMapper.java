package com.blockwit.bwf.model.mapping;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;
import java.util.Optional;

public interface IMapper<FROM, TO> {

  public static final String datetimePattern = "yyyy-MM-dd HH:mm:ss";

  public static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd.MM.yy HH:mm:ss");

  Optional<TO> map(FROM from, Object context);

  List<TO> map(List<FROM> from, Object context);

}
