<template>
<div>
  <h1>TodoList</h1>
  <h5>Welcome to Shinyook's TodoListApp</h5>
  <div class="container">
    <div>
      <b-row>
        <b-col sm="10">
          <b-form-group
            id="fieldset-horizontal"
            label-cols-sm="4"
            label-cols-lg="3"
            label="What to do?"
            label-for="input-horizontal"
          >
          <b-form-input id="input-horizontal" v-model="todo.content" @keyup.enter="addTodo"></b-form-input>
          </b-form-group>
        </b-col>
        <b-col sm="2">
          <b-button variant="outline-primary" @click="addTodo">Add</b-button>
        </b-col>
      </b-row>
    </div>

    <div >
      <b-list-group v-if="todoItem && todoItem.length" >
        <b-list-group-item
        class="list"
        v-for="todo of todoItem"
        v-bind:data="todo.content"
        v-bind:key="todo.id"
        >
        {{todo.content}}
        <b-button variant="success" @click="completeTodo(todo.id)">완료</b-button>
        <b-button variant="primary" @click="modifyTodo(todo.content, todo.id)">수정</b-button>
        <b-button variant="danger" @click="deleteTodo(todo.id)">삭제</b-button>
        </b-list-group-item>
      </b-list-group>
    </div>
    <div v-if="modify">
      <b-row class="my-1">
      <b-col sm="12">
        <b-form-input id="input-large" size="lg" v-bind:placeholder=this.modifyBoard.content @keyup.enter="modifyList" v-model="modifyBoard.content"></b-form-input>
      </b-col>
      </b-row>
    </div>
  </div>

</div>
</template>

<script>
import axios from 'axios';
export default {
created() {
  axios.get('/api/boards')
        .then((res) => {
          res.data.forEach(todo => {
            this.todoItem.push(todo);
          });
        })
        .catch(err => {
          console.error(err);
        })
},
data() {
  return {
    todoItem: [],
    todo: {
      id: '',
      content: ''
    },
    modify: false,
    modifyBoard: {
      id: '',
      content: ''
    }
  }
},
methods: {
  addTodo() {
    if(this.todo.content == '') return; // 공백은 받지 않는다.
    axios.post('/api/boards', this.todo)
    .then(() => {
      this.init();
      this.fetchList();
    })
    .catch(err => {
      console.error(err);
    })
  },
  completeTodo(key) {
    this.deleteTodo(key);
  },
  deleteTodo(key) {
    axios.delete('/api/boards/' + key)
    .then(() => {
      this.init();
      this.fetchList();
    })
    .catch(err => {
      console.error(err);
    })
  },
  modifyTodo(data, id) {
    this.modify = true;
    this.modifyBoard.content = data;
    this.modifyBoard.id = id;
  },
  modifyList() {
    axios.put('/api/boards/'+ this.modifyBoard.id, this.modifyBoard)
    .then(() => {
      this.init();
      this.fetchList();
      this.modify = false;
    })
    .catch(err => {
      console.error(err);
    })
  },
  fetchList() {
    let len = this.todoItem.length;
    this.todoItem.splice(0, len);
    axios.get('/api/boards/')
    .then((res) => {
      res.data.forEach(todo => {
        this.todoItem.push(todo);
      });
    })
    .catch(err => {
      console.error(err);
    })
  },
  init() {
    this.todo = {
      id: '',
      content: ''
    }
  }
}
}
</script>

<style>
.container {
  padding-top: 50px;
  width: 700px;
}
.list{
  text-align: left;
  font-size: 28px;
}
.list button{
  float: right;
  margin: 0 3px;
}
</style>
