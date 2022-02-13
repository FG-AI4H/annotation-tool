import {makeStyles} from "@material-ui/core/styles";

export const ociStyles = makeStyles((theme) => ({
    root: {
        display: 'flex',
    },
    title: {
        flexGrow: 1,
        marginLeft: '15px',
        fontWeight: 'bold',
    },

    content: {
        flexGrow: 1,
        height: '100vh',
        overflow: 'auto',
    },
    container: {
        paddingTop: theme.spacing(4),
        paddingBottom: theme.spacing(4),
    },
    paper: {
        padding: theme.spacing(2),
        display: 'flex',
        overflow: 'auto',
        flexDirection: 'column',
    },
    fixedHeight: {
        height: 240,
    },
}));
