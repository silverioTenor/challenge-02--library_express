package org.libraryexpress.application.book.mapper;

import org.libraryexpress.application.book.dto.request.RegisterBookDto;
import org.libraryexpress.application.book.dto.response.BookDto;
import org.libraryexpress.domain.book.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    BookDto toResponseDto(Book book);

    Book.Builder toEntity(RegisterBookDto dto);
}
