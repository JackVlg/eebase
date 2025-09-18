package tech.eebase.directories;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import tech.eebase.directories.annotations.Directory;
import tech.eebase.directories.annotations.DirectoryType;

@Entity
@Table(name = "DIC_CURRENCIES")
@Directory(
        type=DirectoryType.FLAT,
        key="currencies",
        singularName="Currency",
        pluralName="Currencies",
        createTitleName="Create currency",
        editTitleName="Edit currency",
        icon="fa fa-cube",
        weight=100)
public class Currency extends DirectoryBase {

}
