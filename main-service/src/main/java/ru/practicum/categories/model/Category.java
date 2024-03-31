package ru.practicum.categories.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.valid.Create;
import ru.practicum.valid.Update;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "categories")
@Data
@RequiredArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
}
