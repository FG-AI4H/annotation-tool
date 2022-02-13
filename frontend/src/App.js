import './App.css';
import React from 'react';
import {withAuthenticator} from '@aws-amplify/ui-react';
import "react-loader-spinner/dist/loader/css/react-spinner-loader.css";

import Home from './Home';
import DataStoreHome from './DataStoreHome';
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom';
import TaskList from './TaskList';
import TaskEdit from "./TaskEdit";
import CampaignList from './CampaignList';
import CampaignEdit from "./CampaignEdit";
import AnnotationHome from "./AnnotationHome";
import ImageViewer from "./ImageViewer";
import Profile from "./Profile";
import AdminHome from "./AdminHome";
import UserManagement from "./UserManagement";
import UserEdit from "./UserEdit";
import CssBaseline from '@mui/material/CssBaseline';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import {deepOrange, grey, teal } from "@mui/material/colors";

const theme = createTheme({
    palette: {
        mode: 'dark',
        primary: {
            main: 'rgba(255, 255, 255, 0.87)'
        },
        text: {
            primary: '#fff',
            secondary: grey[500],
        },
    }
});


class App extends React.Component {

  render() {
    return (
        <ThemeProvider theme={theme}>
            <CssBaseline />
        <Router>

              <Switch>
                <Route path='/' exact={true} component={Home}/>
                <Route path='/dashboard' exact={true} component={DataStoreHome}/>

                <Route path='/annotation' exact={true} component={AnnotationHome}/>
                <Route path='/tasks' exact={true} component={TaskList}/>
                  <Route path='/myTasks' exact={true} render={(props) => (
                      <TaskList {...props} me={true} />
                  )}/>
                <Route path='/tasks/:id' component={TaskEdit}/>
                <Route path='/campaigns' exact={true} component={CampaignList}/>
                <Route path='/campaigns/:id' component={CampaignEdit}/>
                <Route path='/imageViewer' component={ImageViewer}/>
                <Route path='/imageViewer/:id' component={ImageViewer}/>
                <Route path='/benchmark' component={() => {
                    window.location.href = 'https://health.aiaudit.org/';
                    return null;
                }}/>
                  <Route
                      exact
                      path="/profile"
                      component={Profile}
                  />
                  <Route path='/admin' exact={true} component={AdminHome}/>
                  <Route path='/userManagement' exact={true} component={UserManagement}/>
                  <Route path='/users/:id' component={UserEdit}/>
              </Switch>

        </Router>
        </ThemeProvider>
    );
  }
}

export default withAuthenticator(App);
