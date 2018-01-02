import PropTypes from 'prop-types';
import {requireNativeComponent, ViewPropTypes} from 'react-native';

var iface = {
  name: 'RCTMusicPlayer',
  propTypes: {
    beginPlay: PropTypes.bool,
    ...ViewPropTypes, // include the default view properties
  },
};

module.exports = requireNativeComponent('RCTMusicPlayer', iface);