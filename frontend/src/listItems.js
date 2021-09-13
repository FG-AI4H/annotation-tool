import * as React from 'react';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import StorageIcon from '@material-ui/icons/Storage';

export const mainListItems = (
    <div>
        <ListItem button>
            <ListItemIcon>
                <StorageIcon />
            </ListItemIcon>
            <ListItemText primary="Datasets" />
        </ListItem>
    </div>
);

