package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.model.Mpa;

public class MpaMapper {

    public static Mpa toModel(MpaDto mpaDto) {
        if (mpaDto == null) {
            return null;
        }

        Mpa mpa = new Mpa(mpaDto.getId(), mpaDto.getName());

        return mpa;
    }

    public static MpaDto toDto(Mpa mpa) {
        if (mpa == null) {
            return null;
        }

        MpaDto mpaDto = new MpaDto();
        mpaDto.setId(mpa.getId());
        mpaDto.setName(mpa.getName());

        return mpaDto;
    }
}
