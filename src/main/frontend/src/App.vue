<template>
  <div id="app">
    <h1>
      <img src="./assets/logo.svg" alt="Enroller" class="logo">
      System do zapisów na zajęcia
    </h1>
    <div v-if="authenticatedUsername">
      <h2>Witaj {{ authenticatedUsername }}!
        <a @click="logout()" class="float-right  button-outline button">Wyloguj</a>
      </h2>
      <meetings-page :username="authenticatedUsername"></meetings-page>
    </div>
    <div v-else>
      <button @click="registering = false" :class = "!registering?'':'button-clear'">Loguje się</button>
      <button @click="registering = true" :class = "registering?'':'button-clear'">Rejestruje się</button>
      <div :class="isErrorMessage ? 'red' : 'green'">{{ message }}</div>
        <login-form @login="login($event)" v-if="registering === false"></login-form>
        <login-form @login="register($event)" button-label="Zarejestruj się" v-else></login-form>
    </div>
  </div>
</template>

<script>
    import "milligram";
    import LoginForm from "./LoginForm";
    import MeetingsPage from "./meetings/MeetingsPage";

    export default {
        components: {LoginForm, MeetingsPage},
        data() {
            return {
                authenticatedUsername: "",
                registering: false,
                message: '',
               isErrorMessage: false,
            };
        },
        methods: {
            login(user) {
                this.authenticatedUsername = user.login;
            },
            logout() {
                this.authenticatedUsername = '';
            },
          register(user) {
            this.$http.post('participants', user)
                .then(response => {
                  // udało się
                  this.message = 'udało się';
                  this.isErrorMessage = false;
                })
                .catch(response => {
                  // nie udało sie
                  this.message = 'nie udało się';
                  this.isErrorMessage = true;
                });
          }
        }
    };
</script>

<style>
  #app {
    max-width: 1000px;
    margin: 0 auto;
  }

  .logo {
    vertical-align: middle;
  }
  .green {background: rgb(181, 194, 64)
  }
  .red {background: red}
</style>

