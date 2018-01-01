import React, { Component } from 'react';
import {
  Platform,
  StyleSheet,
  Button,
  FlatList,
  Text,
  View,
  NativeModules,
  TouchableOpacity,
  Image
} from 'react-native';

class MyListItem extends Component {
  render() {
    return (
      <View>
        <TouchableOpacity>
          <View style={styles.item}>
            <Text style={styles.itemTitle}>{this.props.up.title}</Text>
          </View>
        </TouchableOpacity>
        <TouchableOpacity>
          <View style={styles.item}>
            <Text style={styles.itemTitle}>{this.props.down.title}</Text>
          </View>
        </TouchableOpacity>
      </View>
    )
  }
}

export default class App extends Component<{}> {

  constructor(props){
    super(props)
  }

  onButtonPress() {

  }

  _onMonitorPress = () => {
    NativeModules.SnapshotMonitor.show()
  }

  _renderItem = ({item}) => (
    <MyListItem
      id={item.key}
      up={item.up}
      down={item.down}
    />
  );

  render() {
    return (
      <View style={styles.container}>
        <View style={styles.menu}>
          <Button onPress={this.onButtonPress} title="请选择孩子的年龄"/>
          <Button onPress={this.onButtonPress} title="播放历史"/>
          <Button onPress={this.onButtonPress} title="订阅"/>
          <Button onPress={this.onButtonPress} title="下载"/>
          <Button onPress={this._onMonitorPress} title="截图监测" color="red" />
        </View>
        <View style={styles.content}>
          <View style={styles.leftMenu}>
            <View style={[styles.leftsubmenu, styles.leftsubmenu1]}>
              <Button onPress={this.onButtonPress} title="童话故事"/>
            </View>
            <View style={[styles.leftsubmenu, styles.leftsubmenu2]}>
              <Button onPress={this.onButtonPress} title="儿歌童谣"/>
            </View>
            <View style={[styles.leftsubmenu, styles.leftsubmenu3]}>
              <Button onPress={this.onButtonPress} title="国学诗词"/>
            </View>
            <View style={[styles.leftsubmenu, styles.leftsubmenu4]}>
              <Button onPress={this.onButtonPress} title="英语启蒙"/>
            </View>
            <View style={[styles.leftsubmenu, styles.leftsubmenu5]}>
              <Button onPress={this.onButtonPress} title="趣味科学"/>
            </View>
          </View>
          <View style={styles.itemList}>
            <FlatList
              ref='flatList'
              data={[
                {key: '1', up:{title: 'Devin'}, down:{title: 'Iron'}},
                {key: '2', up:{title: 'Jackson'}, down:{title: 'James'}},
                {key: '3', up:{title: 'Joel'}, down:{title: 'John'}},
                {key: '4', up:{title: 'Herbert'}, down:{title: 'Tom'}},
                {key: '5', up:{title: 'May'}, down:{title: 'Ray'}},
                {key: '6', up:{title: 'TextView'}, down:{title: 'Button'}},
                {key: '7', up:{title: 'View'}, down:{title: 'ViewGroup'}},
                {key: '8', up:{title: 'ScrollView'}, down:{title: 'ListView'}},
                {key: '9', up:{title: 'Image'}, down:{title: 'ImageView'}},
                {key: '10', up:{title: 'ImageButton'}, down:{title: 'LinearLayout'}},
              ]}
              horizontal={true}
              renderItem={this._renderItem}
              onEndReachedThreshold={0.1}
              onEndReached={(info) => {
                alert("滑动到底部了")
              }}
              />
          </View>
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  menu: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    padding: 20
  },
  submenu: {

  },
  content: {
    flexDirection: 'row'
  },
  leftMenu: {
    width: 100,
    marginLeft: 20,
    marginTop: 20,
  },
  leftsubmenu: {
    marginBottom: 15,
  },
  leftsubmenu1: {
    transform: [{skewY: '-5deg'}]
  },
  leftsubmenu3: {
    transform: [{skewY: '5deg'}]
  },
  leftsubmenu4: {
    transform: [{skewY: '8deg'}]
  },
  itemList: {
    paddingLeft: 20,
    paddingRight: 20,
    flex: 1
  },
  item: {
    width: 100,
    height: 100,
    backgroundColor: 'red',
    marginRight: 20,
    marginTop: 20,
    justifyContent: 'center'
  },
  itemTitle: {
    color: 'white',
    alignSelf: 'center'
  },
  popup: {
    position: 'absolute'
  }
});
